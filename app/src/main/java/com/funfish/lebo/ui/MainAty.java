package com.funfish.lebo.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.funfish.lebo.AppConfig;
import com.funfish.lebo.R;
import com.funfish.lebo.interfaces.ApplicationCallbackInterface;
import com.huashizhibo.app.AppContext;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


import io.dcloud.feature.sdk.DCUniMPSDK;


public class MainAty extends MyBaseAty implements ApplicationCallbackInterface {

    /**
     * 1 权限请求                      sdk初始化
     * 2 sdk初始化成功
     * 3 获取版本信息
     */

    private static final int REQUESTCODE = 1001;
    private List<String> mPermissionList = new ArrayList<>();
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};

    private boolean isEndbleOpen = false;
    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        down_pb = findViewById(R.id.down_pb);
        down_lb = findViewById(R.id.lb);
        myApp = (AppContext) getApplication();
        myApp.setAppcationCallbackInterface(this);

        DCUniMPSDK.getInstance().setOnUniMPEventCallBack((event, data, callback) -> {
            JSONObject sendjson = new JSONObject();
            switch (event) {
                case "getServerUrl":
                    sendjson.put("code", 0);
                    sendjson.put("url", AppConfig.mainUrl);
                    callback.invoke(sendjson);
                    break;
                default:
                    break;
            }
            sendjson.put("code", 0);
            callback.invoke(sendjson);
        });

    }

    @Override
    public void initSuccessCallback() {
        if (isEndbleOpen) {
            onChoseurl();
        }
    }

    public void requestPermission() {
        down_lb.setText("权限请求中...");
        mPermissionList.clear();
        for (int i = 0; i < PERMISSIONS_STORAGE.length; i++) {
            if (ContextCompat.checkSelfPermission(this, PERMISSIONS_STORAGE[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(PERMISSIONS_STORAGE[i]);
            }
        }
        if (mPermissionList.isEmpty() || mPermissionList.size() == 0 || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            isEndbleOpen = true;
            initSdk();
        } else {
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(this, permissions, REQUESTCODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUESTCODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                    if (showRequestPermission) {
                        isEndbleOpen = false;
                        requestPermission();
                        LogUtil.e("Permissions===001");
                    } else {
                        isEndbleOpen = false;
                        LogUtil.e("Permissions===002");
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setCancelable(false);
                        builder.setTitle("开启权限");
                        builder.setMessage("为了您更好的使用软件，需要开启以下权限：\n\n" +
                                "访问文件权限");
                        builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent();
                                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                i.setData(uri);
                                startActivity(i);
                                dialog.dismiss();
                            }
                        });
                        dialog = builder.show();
                    }
                } else {
                    isEndbleOpen = true;
                }
            }

            if (isEndbleOpen) {
                initSdk();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppConfig.main_type = 0;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppConfig.main_type = 1;
        requestPermission();
    }

}
