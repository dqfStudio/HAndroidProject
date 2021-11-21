package com.funfish.lebo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.funfish.lebo.AppManager;
import com.funfish.lebo.AppStatusManager;
import com.funfish.lebo.net.ApiListener;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.Map;

import io.dcloud.feature.sdk.DCUniMPSDK;

public class SuperBaseAty  extends AutoLayoutActivity implements ApiListener {

    @Override
    public void onCancelled(Callback.CancelledException var1) {

    }

    @Override
    public void onComplete(RequestParams var1, String var2, String type) {

    }

    @Override
    public void onError(Map<String, String> var1, RequestParams var2) {

    }

    @Override
    public void onExceptionType(Throwable var1, RequestParams params, String type) {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setStatusBarTranslation();
        AppManager.getInstance().addActivity(this);

        if (!this.isTaskRoot()) { // 判断当前activity是不是所在任务栈的根
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }

        AppStatusManager.getInstance().appStatus = 1;
        if (AppStatusManager.getInstance().appStatus != 1) {
            protectApp();
            return;
        }
    }

    private void restartApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppManager.getInstance().killAllActivity();
                Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        },200);
    }


    public void protectApp() {

        try{
            DCUniMPSDK.getInstance().closeCurrentApp();
        }catch (Exception e){
        }
        restartApp();
//        AppManager.getInstance().killAllActivity();
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void setStatusBarTranslation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 实现透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
