package com.funfish.lebo.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.funfish.lebo.AppConfig;
import com.funfish.lebo.BuildConfig;
import com.funfish.lebo.R;
import com.funfish.lebo.download.DownApkUtils;
import com.funfish.lebo.download.DownPluginUtils;
import com.funfish.lebo.interfaces.PluginCallbackInterface;
import com.funfish.lebo.net.AppNet;
import com.funfish.lebo.utils.JSONUtils;
import com.funfish.lebo.utils.SpUtils;
import com.funfish.lebo.utils.ToolUtils;
import com.huashizhibo.app.AppContext;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

import io.dcloud.feature.sdk.DCUniMPSDK;

public class MyBaseAty extends SuperBaseAty implements PluginCallbackInterface {

    public View viewBg;
    public TextView down_lb;
    public ProgressBar down_pb;
    public Dialog dialogUpdate;
    public Dialog dialogGetYing;
    public Dialog dialogGetNetinfo;
    public ImageView imgv_down_loading;

    public AppNet appNet;
    public AppContext myApp;
    public Animation animation;
    public DownApkUtils downApkUtils;
    public DownPluginUtils downPluginUtils;

    private Map<String, String> mapApp_info;       //主包信息
    private Map<String, String> mapApplets_info;   //小程序信息
    public ArrayList array_url = new ArrayList();

    public String new_h5_id = "1.0.0";
    public String app_version_id = BuildConfig.VERSION_NAME;
    public String h5_version_id = BuildConfig.AppletsVersion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
        appNet = new AppNet();

        String[] str =BuildConfig.FormalUrl.split(",");
        for (int i=0;i<str.length;i++){
            array_url.add(str[i]);
        }

        viewBg = findViewById(R.id.v_bg);
        imgv_down_loading = findViewById(R.id.down_loading);
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_01);
        animation.setInterpolator(new LinearInterpolator());

    }

    public void initSdk() {
        if (DCUniMPSDK.getInstance().isInitialize()) {
            onChoseurl();
        } else {
            down_lb.setText("初始化中..");
            myApp.initSdk();
        }
    }

    public void onChoseurl() {
        down_lb.setText("域名解析中..");
        switch (BuildConfig.DevelopType) {
            case "INTEST":
                AppConfig.mainUrl = BuildConfig.InTestUrl;
                getlocalConfig();
                break;
            case "OUTEST":
                AppConfig.mainUrl = BuildConfig.OutTestUrl;
                getlocalConfig();
                break;
            case "FORMAL":
                getYumingConfig();
                break;
            default:
                break;
        }
    }

    /**
     * 获取本地节点
     */
    public void getYumingConfig() {
//        String[] yumingList = ToolUtils.getYumingTxt(this);
//        if (yumingList != null) { //文件读取成功
//
//            if (yumingList.length > 0) {   //取到本地文件
//                array_url.clear();
//                for (int i = 0; i < yumingList.length; i++) {
//                    array_url.add(yumingList[i]);
//                }
//                AppConfig.mainUrl = "https://" + array_url.get(0).toString();
//                getlocalConfig();
//
//            } else {   //本地文件没有
//                //去微博+
//                System.out.println("*****读取域名配置文件内容为空 去微博*****");
//                getYuMing();
//            }
//
//        } else {   //文件读取失败   第一次打开
//            if (array_url.size() == 0) {   //内存没有  取网络
//                getYuMing();
//            } else {    //取内存  第一次进来文件没找到  取内存
//                AppConfig.mainUrl = "https://" + array_url.get(0);
//                getlocalConfig();
//                return;
//            }
//        }

        String[] yumingList = ToolUtils.getYumingTxt(this);
        if (yumingList != null && yumingList.length > 0) { //文件读取成功
            array_url.clear();
            for (int i = 0; i < yumingList.length; i++) {
                array_url.add(yumingList[i]);
            }
            AppConfig.mainUrl = "https://" + array_url.get(0).toString();
            getlocalConfig();
        }else if (array_url.size() > 0) {
            //取内存  第一次进来文件没找到  取内存
            AppConfig.mainUrl = "https://" + array_url.get(0);
            getlocalConfig();
        }else {
            //内存没有  取网络
            getYuMing();
        }
    }

    /**
     * 获取本地小程序版本号
     */
    private void getlocalConfig() {
        String str = SpUtils.get(this, "config.txt", "").toString();
        if (!TextUtils.isEmpty(str)) {
            h5_version_id = str;
        } else {
            SpUtils.put(this, "config.txt", str);
        }
        getAppVersion();
    }

    /**
     * 获取Apk版本信息
     */
    private void getAppVersion() {
        down_lb.setText("版本验证...");
        appNet.getNetinfo(AppConfig.mainUrl, this);
    }

    /**
     * 从微博获取新的节点
     */
    private void getYuMing() {
        down_lb.setText("重新获取新节点...");
        appNet.getYuMing(this);
    }

    /**
     * 下载apk
     * @param url
     */
    public void downApk(String url) {
        downApkUtils = new DownApkUtils(url, down_lb, down_pb);
        downApkUtils.startDownload();
    }

    /**
     * 下载小程序
     *
     * @param url
     */
    public void downPlugin(String url, String wgtName, String new_h5_id) {
        downPluginUtils = new DownPluginUtils(url, wgtName, down_lb, down_pb);
        downPluginUtils.setNew_h5_id(new_h5_id);
        downPluginUtils.setPluginCallbackInterface(this);
        downPluginUtils.startDownload();
    }

    /**
     * 小程序下载成功
     *
     * @param status
     */
    @Override
    public void callbackPlugin(int status) {
        switch (status) {
            case 6:
                down_lb.setText("启动程序中...");
                break;
            case -999:
                Toast.makeText(MyBaseAty.this, "请重新启动！", Toast.LENGTH_SHORT).show();
                protectApp();
                break;
            default:
                break;
        }
    }

    /**
     * 小程序下载成功
     */
    @Override
    public void callbackPlugin2() {
        toLive();
    }


    private void toLive() {
        toLiveMain();
    }

    /**
     * 打开小程序
     */
    private void toLiveMain() {

        try {
            String name =(SpUtils.get(this, "package_names_a","")).toString();
            DCUniMPSDK.getInstance().startApp(this, name);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(RequestParams var1, String var2, String type) {
        super.onComplete(var1, var2, type);

        if (AppConfig.main_type == 0) {
            return;
        }

        if (type.equals("getNetinfo")) {
            ToolUtils.creatYumingConfig(array_url);
            String str2 = new String(Base64.decode(var2.replace("molixg", ""), Base64.DEFAULT));
            System.out.println("*****FFFF2*****" + str2);
            Map<String, String> map = JSONUtils.parseKeyAndValueToMap(str2);
            Map<String, String> mapData = JSONUtils.parseDataToMap(str2);
            mapApplets_info = JSONUtils.parseKeyAndValueToMap2(mapData.get("applets_info"));
//            if (mapData.containsKey("app_info")) {
//                mapApp_info = JSONUtils.parseKeyAndValueToMap(mapData.get("app_info"));
//            }
            if (map.get("code").equals("0")) {
                new_h5_id = mapApplets_info.get("current_version");
//                if (mapApp_info == null) {
//                    System.out.println("*****FFFF3*****");
//                    checkAppVersion(app_version_id, new_h5_id, "0", "");
//                } else {
//                    System.out.println("*****FFFF4*****");
//                    String txt_des = mapApp_info.get("description");
//                    String app_id = mapApp_info.get("current_version");
//                    String code_status = mapApp_info.get(git"code_status");
//                    checkAppVersion(app_id, new_h5_id, code_status, txt_des);
//                }
                if (mapData.containsKey("app_info")) {
                    mapApp_info = JSONUtils.parseKeyAndValueToMap(mapData.get("app_info"));
                    String txt_des = mapApp_info.get("description");
                    String app_id = mapApp_info.get("current_version");
                    String code_status = mapApp_info.get("code_status");
                    checkAppVersion(app_id, new_h5_id, code_status, txt_des);
                }else {
                    //判断是否更新h5
                    checkH5Version(new_h5_id);
                }

            } else {

                if (dialogGetNetinfo != null && !dialogGetNetinfo.isShowing()) {
                    dialogGetNetinfo.show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("错误");
                builder.setMessage("下载失败，请检查您的网络连接");
                builder.setPositiveButton("重试", (dialog, which) -> {
                    dialog.dismiss();
                    getAppVersion();
                });
                dialogGetNetinfo = builder.show();
            }
        }

        if (type.equals("getYuMing")) {
            array_url = ToolUtils.getToken(var2);
            if (array_url.size() > 0) {
                AppConfig.mainUrl = "https://" + array_url.get(0);
                getAppVersion();
            } else {
                down_lb.setText("加载数据中...");
            }
        }
    }


    @Override
    public void onExceptionType(Throwable ex, RequestParams params, String type) {
        super.onExceptionType(ex, params, type);
        if (AppConfig.main_type == 0) {
            return;
        }

        if (type.equals("getNetinfo")) {

            if (ToolUtils.isNetworkConnected()) {  // IP被封
                if (array_url != null && array_url.size() > 0) {
                    array_url.remove(0);
                }
                if (array_url.size() > 0) {
                    AppConfig.mainUrl = "https://" + array_url.get(0);
                    getAppVersion();
                } else {
                    LogUtil.e("============" + "从微博获取");
                    getYuMing();
                }

            } else {    //无网络
                if (dialogGetNetinfo != null && !dialogGetNetinfo.isShowing()) {
                    dialogGetNetinfo.show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("错误");
                builder.setMessage("下载失败，请检查您的网络连接");
                builder.setPositiveButton("重试", (dialog, which) -> {
                    dialog.dismiss();
                    getAppVersion();
                });
                dialogGetNetinfo = builder.show();
            }
        }

        if (type.equals("getYuMing")) {
            if (ToolUtils.isNetworkConnected()) {  //微博获取失败
                down_lb.setText("加载数据中...");
            } else {
                if (dialogGetYing != null && !dialogGetYing.isShowing()) {
                    dialogGetYing.show();
                    ;
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("错误");
                builder.setMessage("下载失败，请检查您的网络连接");
                builder.setPositiveButton("重试", (dialog, which) -> {
                    dialog.dismiss();
                    getYuMing();
                });
                dialogGetYing = builder.show();
            }
        }
    }

    public void checkAppVersion(String app_id, String new_h5_id, String status, String txt_des) {
        //判断是否更新apk
//        if (ToolUtils.isGengXin(app_version_id, app_id)) {
//            if (status.equals("10001")) {  //不强跟
//                showBuilder2(txt_des, mapApp_info.get("url"), false);
//            } else {
//                showBuilder2(txt_des, mapApp_info.get("url"), true);
//            }
//            return;
//        }
//        //判断是否更新h5
//        checkH5Version(new_h5_id);
        if (status.equals("10001")) {  //不强跟
            showBuilder2(txt_des, mapApp_info.get("url"), false);
        } else {
            showBuilder2(txt_des, mapApp_info.get("url"), true);
        }
    }

    public void checkH5Version(String new_h5_id) {

        LogUtil.e("checkH5Version new_h5_id===" + new_h5_id);
        LogUtil.e("checkH5Version h5_version_id===" + h5_version_id);

        if (!ToolUtils.isGengXin2(h5_version_id,new_h5_id)) {
            LogUtil.e("checkH5Version 不需要更新");
            down_lb.setText("启动程序中...");
            if (!DCUniMPSDK.getInstance().isExistsApp(BuildConfig.AppletsName)) {
                LogUtil.e("checkH5Version 不存在");
                if (!ToolUtils.isGengXin2(BuildConfig.AppletsVersion,new_h5_id)) {
                    LogUtil.e("checkH5Version 不存在 从本地更新");
                    String assetsToPath = ToolUtils.copyAssetAndWrite(MyBaseAty.this, BuildConfig.AppletsName+".wgt");
                    if (assetsToPath == null) {
                        LogUtil.e("checkH5Version 不存在 从本地更新 从后台更新");
                        downPlugin(mapApplets_info.get("url"), mapApplets_info.get("package_names"), new_h5_id);
                        return;
                    }
                    DCUniMPSDK.getInstance().releaseWgtToRunPathFromePath(BuildConfig.AppletsName, assetsToPath, (code, pArgs) -> {
                        if (code == 1) {
                            //每次释放小程序资源成功 保存版本号
                            SpUtils.put(x.app(), "config.txt", BuildConfig.AppletsVersion);
                            checkH5Version2();
                        } else {
                            System.out.println("释放wgt失败");
                        }
                        return null;
                    });
                } else {
                    downPlugin(mapApplets_info.get("url"), mapApplets_info.get("package_names"), new_h5_id);
                }

            } else {//打开存在的小程序
                checkH5Version2();
            }

        } else {
            LogUtil.e("checkH5Version 需要更新");
            downPlugin(mapApplets_info.get("url"), mapApplets_info.get("package_names"), new_h5_id);
        }
    }

    public void checkH5Version2() {
        if (DCUniMPSDK.getInstance().isInitialize()) {
            SpUtils.put(MyBaseAty.this, "package_names_a", mapApplets_info.get("package_names"));
            toLive();
        } else {
            Toast.makeText(MyBaseAty.this, "请重新启动!", Toast.LENGTH_SHORT).show();
            protectApp();
        }
    }

    public void showBuilder2(String text_des, final String url, boolean isCompulsoryUpdate) {

        if (downPluginUtils != null) {
            downPluginUtils.onPause();
        }

        if (downApkUtils != null) {
            downApkUtils.onPause();
        }

        View view = View.inflate(this, R.layout.dlg_update, null);
        TextView tv_content = (TextView) view.findViewById(R.id.update_description);
        TextView tv_no = (TextView) view.findViewById(R.id.buildeexti_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.builderexit_tv_ok);
        View v_line = (View) view.findViewById(R.id.v_line);
        dialogUpdate = new Dialog(this, R.style.dialog);
        if (isCompulsoryUpdate) {
            tv_no.setVisibility(View.GONE);
            v_line.setVisibility(View.GONE);
            tv_ok.setBackground(getResources().getDrawable(R.drawable.shape_01));
        } else {
            tv_no.setVisibility(View.VISIBLE);
            v_line.setVisibility(View.VISIBLE);
            tv_ok.setBackground(getResources().getDrawable(R.drawable.shape_02));
        }
        if (!TextUtils.isEmpty(text_des)) {
            tv_content.setText(text_des);
        }
        tv_no.setOnClickListener(v -> {
            dialogUpdate.cancel();
            checkH5Version(new_h5_id);
        });
        tv_ok.setOnClickListener(v -> {
            dialogUpdate.cancel();
            downApk(url);
        });
        dialogUpdate.setContentView(view);
        dialogUpdate.setCancelable(false);
        dialogUpdate.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        imgv_down_loading.startAnimation(animation);
        if (dialogUpdate != null) {
            dialogUpdate.cancel();
        }
        if (dialogGetNetinfo != null) {
            dialogGetNetinfo.cancel();
        }

        if (dialogGetYing != null) {
            dialogGetYing.cancel();
        }
        if (downApkUtils != null) {
            downApkUtils.onStop();
        }
        if (downPluginUtils != null) {
            downPluginUtils.onStop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        imgv_down_loading.clearAnimation();

        if (dialogUpdate != null) {
            dialogUpdate.cancel();
        }
        if (dialogGetNetinfo != null) {
            dialogGetNetinfo.cancel();
        }

        if (dialogGetYing != null) {
            dialogGetYing.cancel();
        }

        if (downApkUtils != null) {
            downApkUtils.onStop();
        }
        if (downPluginUtils != null) {
            downPluginUtils.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downApkUtils != null) {
            downApkUtils.onDestroy();
        }
        if (downPluginUtils != null) {
            downPluginUtils.onDestroy();
        }
    }

}
