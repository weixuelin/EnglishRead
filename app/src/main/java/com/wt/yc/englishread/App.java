package com.wt.yc.englishread;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.wt.yc.englishread.tts.TTSForApi;
import com.youdao.sdk.app.YouDaoApplication;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        YouDaoApplication.init(this, TTSForApi.Companion.getAppKey());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    static List<Activity> list = new ArrayList<>();

    public static void addActivity(Activity activity) {
        list.add(activity);

    }

    public static void exit() {
        for (Activity temp : list) {
            temp.finish();
        }

        System.exit(0);

    }
}
