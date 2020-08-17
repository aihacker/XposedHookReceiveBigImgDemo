package com.example.xposedhookreceivebigimgdemo.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
//import io.netty.handler.codec.http2.HttpUtil;
import java.util.ArrayList;
import java.util.List;

public class RootAndXposedCheck2 {
    public static void preventDetection(LoadPackageParam loadPackageParam) {
        Util.findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getInstalledPackages", Integer.TYPE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(RootAndXposedCheck2.packageFilter((List) methodHookParam.getResult()));
            }
        });
        Util.findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getPackagesHoldingPermissions", String[].class, Integer.TYPE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(RootAndXposedCheck2.packageFilter((List) methodHookParam.getResult()));
            }
        });
        Util.findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getPreferredPackages", Integer.TYPE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(RootAndXposedCheck2.packageFilter((List) methodHookParam.getResult()));
            }
        });
        Util.findAndHookMethod("android.app.ApplicationPackageManager", loadPackageParam.classLoader, "getInstalledApplications", Integer.TYPE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(RootAndXposedCheck2.getApplicationInfo((List) methodHookParam.getResult()));
            }
        });
        Util.hookMethod(ActivityManager.class, "getRunningAppProcesses", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                List<RunningAppProcessInfo> list = (List) methodHookParam.getResult();
                if (list != null) {
                    ArrayList arrayList = new ArrayList();
                    for (RunningAppProcessInfo runningAppProcessInfo : list) {
                        if (!RootAndXposedCheck2.packageFilter(runningAppProcessInfo.processName)) {
                            arrayList.add(runningAppProcessInfo);
                        }
                    }
                    methodHookParam.setResult(arrayList);
                }
            }
        });
        Util.hookMethod(ActivityManager.class, "getRunningServices", Integer.TYPE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                List<RunningServiceInfo> list = (List) methodHookParam.getResult();
                if (list != null) {
                    ArrayList arrayList = new ArrayList();
                    for (RunningServiceInfo runningServiceInfo : list) {
                        String str = null;
                        if (runningServiceInfo.service != null) {
                            str = runningServiceInfo.service.getPackageName();
                        }
                        if (str == null) {
//                            str = HttpUtil.OUT_OF_MESSAGE_SEQUENCE_PATH;    //https://blog.csdn.net/tq08g2z/article/details/77311284
                        }
                        if (!RootAndXposedCheck2.packageFilter(str)) {
                            arrayList.add(runningServiceInfo);
                        }
                    }
                    methodHookParam.setResult(arrayList);
                }
            }
        });
    }

    public static boolean packageFilter(String str) {
        return TextUtils.isEmpty(str) ? false : str.contains("com.daxi.") || str.contains("com.jubo.") || str.contains("com.kingroot.") || str.contains("xposed") || str.contains("eu.chainfire.supersu");
    }

    public static List<ApplicationInfo> getApplicationInfo(List<ApplicationInfo> list) {
        if (list == null) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        for (ApplicationInfo applicationInfo : list) {
            if (!packageFilter(applicationInfo.packageName)) {
                arrayList.add(applicationInfo);
            }
        }
        return arrayList;
    }

    public static List<PackageInfo> packageFilter(List<PackageInfo> list) {
        if (list == null) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        for (PackageInfo packageInfo : list) {
            if (!packageFilter(packageInfo.packageName)) {
                arrayList.add(packageInfo);
            }
        }
        return arrayList;
    }
}