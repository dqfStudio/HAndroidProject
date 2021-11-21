package com.funfish.lebo.download;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.funfish.lebo.AppConfig;
import com.funfish.lebo.AppManager;
import com.funfish.lebo.interfaces.LogDownloadListener;
import com.funfish.lebo.interfaces.PluginCallbackInterface;
import com.funfish.lebo.utils.SpUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.lzy.okserver.task.XExecutor;

import org.xutils.x;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.dcloud.feature.sdk.DCUniMPSDK;

public class DownPluginUtils implements XExecutor.OnAllTaskEndListener {

    private OkDownload okDownload;
    private List<DownloadTask> values;
    private List<ApkModel> apks;
    private DownloadTask task;

    private TextView tvProgress;
    private ProgressBar pbProgress;

    private String apkUrl;
    private String apkUrlTag = "";
    private String wgtName = "";
    private String filePath;
    private NumberFormat numberFormat;
    private String new_h5_id;

    private PluginCallbackInterface callbackInterface;

    public void setPluginCallbackInterface(PluginCallbackInterface callbackInterface) {
        this.callbackInterface = callbackInterface;
    }

    public DownPluginUtils(String apkUrl, String wgtName, TextView tvProgress, ProgressBar pbProgress) {
        this.apkUrl = apkUrl;
        this.apkUrlTag = apkUrl;
        this.wgtName = wgtName;
        this.tvProgress = tvProgress;
        this.pbProgress = pbProgress;
        onCreate();
    }

    public void setNew_h5_id(String new_h5_id) {
        this.new_h5_id = new_h5_id;
    }

    public void onCreate() {
        okDownload = OkDownload.getInstance();
        okDownload.setFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/waimao/");
        okDownload.addOnAllTaskEndListener(this);
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(0);
        initData();
        tvProgress.setVisibility(View.VISIBLE);
        pbProgress.setVisibility(View.VISIBLE);
    }

    public void onPause() {
        if (task != null) {
            task.pause();
        }
    }


    private void initData() {
        apks = new ArrayList<>();
        ApkModel apk1 = new ApkModel();
        apk1.name = "";
        apk1.iconUrl = "";
        apk1.url = apkUrl;
        apks.add(apk1);
    }

    @Override
    public void onAllTaskEnd() {
    }

    public void onDestroy() {
        okDownload.removeOnAllTaskEndListener(this);
        Map<String, DownloadTask> taskMap = OkDownload.getInstance().getTaskMap();
        for (DownloadTask task : taskMap.values()) {
            task.unRegister(createTag(task));
        }
    }

    private String createTag(DownloadTask task) {
        return "q_" + task.progress.tag;
    }

    public void startDownload() {
        List<DownloadTask> values = OkDownload.restore(DownloadManager.getInstance().getAll());
        int status = 999;
        int index = -1;
        for (int i = 0; i < values.size(); i++) {
            String tag = values.get(i).progress.tag;
            if (tag.equals(apkUrlTag)) {
                index = i;
                status = values.get(i).progress.status;
                break;
            }
        }

        switch (status) {
            //数据库无任务
            case 999:
                download();
                break;
            case Progress.FINISH:
                pbProgress.setMax(10000);
                pbProgress.setProgress(10000);
                tvProgress.setText("100%");
                filePath = values.get(index).progress.filePath;
                openPlugin();
                break;
            //数据库存在未完成任务
            default:
                getDown();
                break;
        }
    }

    public void download() {

        ApkModel apk = apks.get(0);
        GetRequest<File> request = OkGo.<File>get(apk.url)
                .headers("aaa", "111")
                .params("bbb", "222");

        OkDownload.request(apk.url, request)
                .priority(apk.priority)
                .extra1(apk)
                .save()
                .register(new LogDownloadListener())
                .start();
        getDown();
    }

    public void getDown() {
        values = OkDownload.restore(DownloadManager.getInstance().getAll());
        for (int i = 0; i < values.size(); i++) {
            String tag = values.get(i).progress.tag;
            if (tag.equals(apkUrlTag)) {
                task = values.get(i);
                break;
            }
        }
        task.register(new ListDownloadListener(createTag(task)))//
                .register(new LogDownloadListener());
        task.start();
    }


    private class ListDownloadListener extends DownloadListener {

        ListDownloadListener(Object tag) {
            super(tag);
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
            refresh(progress);
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            filePath = progress.filePath;
            openPlugin();
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }


    public void refresh(Progress progress) {
        switch (progress.status) {
            case Progress.NONE:
                break;
            case Progress.PAUSE:
                break;
            case Progress.ERROR:
                showDialog();
                break;
            case Progress.WAITING:
                break;
            case Progress.FINISH:
                break;
            case Progress.LOADING:
                break;
        }
        tvProgress.setText("下载中..." + numberFormat.format(progress.fraction));
        pbProgress.setMax(10000);
        pbProgress.setProgress((int) (progress.fraction * 10000));
    }

    public Dialog dialog;

    public void onStop() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void showDialog() {
        if (dialog!=null&&!dialog.isShowing()){
            dialog.show();;
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(AppManager.getInstance().getTopActivity());
        builder.setCancelable(false);
        builder.setTitle("错误");
        builder.setMessage("下载失败，请检查您的网络连接");

        builder.setPositiveButton("重试", (dialog, which) -> {
            dialog.dismiss();
            startDownload();
        });
        dialog = builder.show();
    }

    public void openPlugin() {

        if (AppConfig.main_type == 0) {
            return;
        }

        DCUniMPSDK.getInstance().releaseWgtToRunPathFromePath(wgtName, filePath, (code, pArgs) -> {

            if (code == 1) {//释放wgt完成
                //保存小程序版本号
                SpUtils.put(x.app(), "config.txt", new_h5_id);
                if (callbackInterface != null) {
                    callbackInterface.callbackPlugin(6);
                }

                try {
                    if (DCUniMPSDK.getInstance().isInitialize()) {
                        SpUtils.put(x.app(), "package_names_a", wgtName);
                        callbackInterface.callbackPlugin2();
//                        DCUniMPSDK.getInstance().startApp(x.app(), wgtName);
                    } else {

                        if (callbackInterface != null) {
                            callbackInterface.callbackPlugin(-999);
                        }
                    }

                } catch (Exception e) {

                    if (callbackInterface != null) {
                        callbackInterface.callbackPlugin(-999);
                    }
                }

            } else {
                System.out.println("释放wgt失败");
            }
            return null;
        });
    }

}
