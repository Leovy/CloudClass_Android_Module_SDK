//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.allin.commlibrary.system;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.lang.reflect.Method;

public class DeviceUtils {
    private static final String VIVO_NAVIGATION_GESTURE = "navigation_gesture_on";
    private static final String HUAWAI_DISPLAY_NOTCH_STATUS = "display_notch_status";
    private static final String XIAOMI_DISPLAY_NOTCH_STATUS = "force_black";
    private static final String XIAOMI_FULLSCREEN_GESTURE = "force_fsg_nav_bar";

    public DeviceUtils() {
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }

    public static int sp2Px(Context context, float spValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int)TypedValue.applyDimension(2, spValue, metrics);
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (checkDeviceHasNavigationBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }

        return result;
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }

        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String)m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception var7) {
        }

        return hasNavigationBar;
    }


    public static String getDeviceManufacture() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static String getSystemVersion() {
        return VERSION.RELEASE;
    }


    public static boolean vivoNavigationGestureEnabled(Context context) {
        int val = Secure.getInt(context.getContentResolver(), "navigation_gesture_on", 0);
        return val != 0;
    }

    public static boolean xiaomiNavigationGestureEnabled(Context context) {
        int val = Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0);
        return val != 0;
    }

    public static boolean huaweiIsNotchSetToShowInSetting(Context context) {
        int result = Secure.getInt(context.getContentResolver(), "display_notch_status", 0);
        return result == 0;
    }

    @TargetApi(17)
    public static boolean xiaomiIsNotchSetToShowInSetting(Context context) {
        return Global.getInt(context.getContentResolver(), "force_black", 0) == 0;
    }
}
