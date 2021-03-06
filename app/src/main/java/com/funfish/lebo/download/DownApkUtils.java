package com.funfish.lebo.download;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.funfish.lebo.AppManager;
import com.funfish.lebo.interfaces.LogDownloadListener;
import com.funfish.lebo.utils.ToolUtils;
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


public class DownApkUtils implements XExecutor.OnAllTaskEndListener {

    private OkDownload okDownload;
    private List<DownloadTask> values;
    private List<ApkModel> apks;
    private DownloadTask task;

    private TextView tvProgress;
    private ProgressBar pbProgress;

    private String apkUrl;
    private String apkUrlTag = "";
    private File file;
    private NumberFormat numberFormat;

    public DownApkUtils(String apkUrl, TextView tvProgress, ProgressBar pbProgress) {
        this.apkUrl = apkUrl;
        this.apkUrlTag = this.apkUrl;
        this.tvProgress = tvProgress;
        this.pbProgress = pbProgress;
        onCreate();
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
            //??????????????????
            case 999:
                download();
                break;
            case Progress.FINISH:
//                download.setText("??????");
                pbProgress.setMax(10000);
                pbProgress.setProgress(10000);
                tvProgress.setText("100%");
                file = new File(values.get(index).progress.filePath);
                install();
                break;
            //??????????????????????????????
            default:
                getDown();
                break;
        }
    }

    public void install() {
//        ToolUtils.installApk(file,  x.app().getPackageName()+".FileProvider", x.app());
        ToolUtils.installApk(file,  x.app().getPackageName()+".fileprovider", x.app());
    }

    public void onPause() {
        if (task != null) {
            task.pause();
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
        task.register(new ListDownloadListener(createTag(task)))
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
            DownApkUtils.this.file = file;
            install();
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
        tvProgress.setText("?????????..." + numberFormat.format(progress.fraction));
        pbProgress.setMax(10000);
        pbProgress.setProgress((int) (progress.fraction * 10000));
    }

    public Dialog dialog;

    public void showDialog() {
        if (dialog!=null&&!dialog.isShowing()){
            dialog.show();;
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(AppManager.getInstance().getTopActivity());
        builder.setCancelable(false);
        builder.setTitle("??????");
        builder.setMessage("??????????????????????????????????????????");

        builder.setPositiveButton("??????", (dialog, which) -> {
            dialog.dismiss();
            startDownload();
        });
        dialog = builder.show();

    }

    public void onStop(){
        if (dialog!=null){
            dialog.dismiss();
        }
    }
}
