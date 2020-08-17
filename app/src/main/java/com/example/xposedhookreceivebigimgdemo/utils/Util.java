package com.example.xposedhookreceivebigimgdemo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class Util {
    private static boolean isLogOpen = true;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.CHINA);
    public static String getWXVersion;

    public static Class findClass(String str, ClassLoader classLoader) {
        try {
            return XposedHelpers.findClass(str, classLoader);
        } catch (Exception e) {
            XposedBridge.log(e);
            return null;
        }
    }

    public static void hookMethod(Class<?> clazz, String methodName, Object... objArr) {
        try {
            XposedHelpers.findAndHookMethod(clazz, methodName, objArr);
        } catch (Exception e) {
            XposedBridge.log(e);
            logE(e.getMessage());
        } catch (NoSuchMethodError e2) {
            XposedBridge.log(e2);
            logE(e2.getLocalizedMessage());
        }
    }

    public static void logI(String str) {
        if (isLogOpen && str != null) {
            Log.i("MicroMsg.Daxi", str);
        }
    }

    public static void logD(String str) {
        if (isLogOpen && str != null) {
            Log.d("Xposed Daxi", str);
        }
    }

    public static void hookStructor(String str, ClassLoader classLoader, XC_MethodHook xC_MethodHook) {
        try {
            for (Constructor constructor : XposedHelpers.findClass(str, classLoader).getDeclaredConstructors()) {
                constructor.setAccessible(true);
                XposedBridge.hookMethod(constructor, xC_MethodHook);
            }
        } catch (Exception e) {
            XposedBridge.log(e);
            logE(e.getMessage());
        }
    }

    public static void findAndHookMethod(String className, ClassLoader classLoader, String methodName, Object... objArr) {
        try {
            XposedHelpers.findAndHookMethod(className, classLoader, methodName, objArr);
        } catch (Exception | NoSuchMethodError e) {
            XposedBridge.log(e);
            logE(e.getMessage());
        }
    }

    public static void loggerSwitcher(boolean z) {
        isLogOpen = z;
    }

    public static void printStackTrace(String str) {
        try {
            throw new Exception("MicroMsg.Daxi " + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logE(String str) {
        if (str != null) {
            Log.e("MicroMsg.Daxi", str);
        }
    }

    public static String getWXVersion() {
        try {
            getWXVersion = ((Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]), "getSystemContext", new Object[0])).getPackageManager().getPackageInfo("com.tencent.mm", 0).versionName;
        } catch (Exception e) {
        }
        Util.logI("MicroMsg version: " + getWXVersion);
        return getWXVersion;
    }
}
