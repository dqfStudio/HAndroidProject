package com.funfish.lebo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.Iterator;
import java.util.Stack;


public class AppManager {
    private static Stack<Activity> mActivityStack;
    private static AppManager mAppManager;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (mAppManager == null) {
            mAppManager = new AppManager();
        }

        return mAppManager;
    }

    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack();
        }
        mActivityStack.add(activity);
    }

    public Activity getTopActivity() {
        Activity activity = (Activity) mActivityStack.lastElement();
        return activity;
    }

    public void killTopActivity() {
        Activity activity = (Activity) mActivityStack.lastElement();
        this.killActivity(activity);
    }

    public void killActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
        }

    }
    public void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
//            activity.finish();
        }
    }

    public void killtoActivity(Class<?> cls) {
        Iterator iterator = mActivityStack.iterator();

        while (iterator.hasNext()) {
            Activity activity = (Activity) iterator.next();
            if (!activity.getClass().equals(cls)) {
                iterator.remove();
                activity.finish();
            }
        }
    }

    public int SelectActivity(Class<?> cls) {
        int index = 0;
        Iterator iterator = mActivityStack.iterator();

        while (iterator.hasNext()) {
            Activity activity = (Activity) iterator.next();
            if (activity.getClass().equals(cls)) {
                index = 1;
            }
        }
        return index;
    }

    public Activity selectActivity(Class<?> cls) {

        Activity mActivity = null;
        Iterator iterator = mActivityStack.iterator();

        while (iterator.hasNext()) {
            Activity activity = (Activity) iterator.next();
            if (activity.getClass().equals(cls)) {
                mActivity = activity;
            }
        }
        return mActivity;
    }


    public void killActivity(Class<?> cls) {
        if (mActivityStack==null||mActivityStack.size()==0){
            Log.e("killActivity ","null");
            return;
        }
        Iterator iterator = mActivityStack.iterator();

        while (iterator.hasNext()) {
            Activity activity = (Activity) iterator.next();
            if (activity.getClass().equals(cls)) {
                Log.e("killActivity ",cls.toString());

                iterator.remove();
                activity.finish();
            }
        }

    }

    public void killOtherActivity(Class<?> cls) {
        Iterator iterator = mActivityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = (Activity) iterator.next();
            if (!activity.getClass().equals(cls)) {
                iterator.remove();
                activity.finish();
            }
        }
    }


    public void killAllActivity() {
        if (mActivityStack==null){
            return;
        }
        int i = 0;
        for (int size = mActivityStack.size(); i < size; ++i) {
            if (null != mActivityStack.get(i)) {
                ((Activity) mActivityStack.get(i)).finish();
            }
        }
        mActivityStack.clear();
    }

    public void AppExit(Context context) {
        try {
            this.killAllActivity();
            ActivityManager e = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            e.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception var3) {
        }

    }
}
