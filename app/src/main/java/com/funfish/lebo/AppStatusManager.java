package com.funfish.lebo;

public class AppStatusManager {

    public static int appStatus= -1;        //APP状态 初始值为没启动 不在前台状态

    public static AppStatusManager appStatusManager;

    private AppStatusManager() {

    }

    public static AppStatusManager getInstance() {
        if (appStatusManager == null) {
            appStatusManager = new AppStatusManager();
        }
        return appStatusManager;
    }


    public int getAppStatus() {
        return appStatus; }
    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }
}
