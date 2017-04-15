package com.ming.test.host;

import com.ming.mplugin.PluginManager;

import android.app.Application;
import android.util.DisplayMetrics;

public class HostApplication extends Application {
    private static HostApplication application;

    private int screenWidth;
    private int screenHeight;
    private float density;
    private float xdpi;
    private float ydpi;

    public static HostApplication getApplication() {
        return application;
    }


    public int getScreenWidth() {
        return screenWidth;
    }


    public int getScreenHeight() {
        return screenHeight;
    }


    public float getDensity() {
        return density;
    }


    public float getXdpi() {
        return xdpi;
    }


    public float getYdpi() {
        return ydpi;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        // 读取屏幕相关信息
        DisplayMetrics dm = new DisplayMetrics();
        dm = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        density = dm.density;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;

        PluginManager.initPlugManager(application);
    }
}
