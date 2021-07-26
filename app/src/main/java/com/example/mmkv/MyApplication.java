package com.example.mmkv;

import android.app.Application;
import android.os.HandlerThread;
import android.os.Looper;

import com.tencent.mmkv.MMKV;

public class MyApplication extends Application {

    private HandlerThread mThread;

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);
        instance = this;

        mThread = new HandlerThread("handler_thread");
        mThread.start();
    }

    public static MyApplication getInstance(){
        return instance;
    }

    public Looper getmThread(){
        return mThread.getLooper();
    }
}
