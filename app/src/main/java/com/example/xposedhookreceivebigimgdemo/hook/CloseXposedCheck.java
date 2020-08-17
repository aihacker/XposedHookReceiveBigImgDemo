package com.example.xposedhookreceivebigimgdemo.hook;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.xposedhookreceivebigimgdemo.utils.RootAndXposedCheck2;
import com.example.xposedhookreceivebigimgdemo.utils.Util;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
 * @author glsite.com
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class CloseXposedCheck {

    private static final String TAG = "hacker.CloseXposedCheck";

    public CloseXposedCheck(ClassLoader classLoader) {
        //b(StackTraceElement[] arg6)
        XposedHelpers.findAndHookMethod("com.tencent.mm.app.u",classLoader, "b",
        StackTraceElement.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Log.i(TAG,param.args[0].toString());
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Log.i(TAG,"com.tencent.mm.app.u.a_isXposedPlugins()返回值: " +false);
                        param.setResult(false);
                    }
                });

        //=================================================
        final String METHODTAG = "[preventDetection]";
        final String path = Environment.getExternalStorageDirectory().getPath();
        try {
            Object[] objectArray = {new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam methodHookParam) {
                    StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) methodHookParam.getResult();
                    ArrayList arrayList = new ArrayList();
                    for (StackTraceElement stackTraceElement : stackTraceElementArr) {
                        if (!stackTraceElement.getClassName().toLowerCase().contains("xposed")) {
                            arrayList.add(stackTraceElement);
                        }
                    }
                    methodHookParam.setResult(arrayList.toArray(new StackTraceElement[0]));
                }
            }};
            if (XposedHelpers.findMethodExactIfExists(Throwable.class, "getOurStackTrace", new Object[0]) != null) {
                XposedHelpers.findAndHookMethod(Throwable.class, "getOurStackTrace", objectArray);
            } else {
                XposedHelpers.findAndHookMethod(Throwable.class, "getInternalStackTrace", objectArray);
            }
            XposedHelpers.findAndHookMethod(Thread.class, "getStackTrace", objectArray);

            XposedHelpers.findAndHookConstructor(File.class, String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam methodHookParam) {
                    String str = (String) methodHookParam.args[0];
                    if (str.endsWith("/su")) {
                        methodHookParam.args[0] = "/system/bin/suxx";
                        return;
                    }
                    int i = (str.matches("/proc/[0-9]+/maps") || !(!str.toLowerCase().contains("xposed") || str.startsWith(path) || str.contains("fkzhang"))) ? 1 : 0;
                    if (i != 0) {
                        methodHookParam.args[0] = "/system/build.prop";
                    }
                }
            });

            XposedHelpers.findAndHookConstructor(File.class, String.class, String.class, new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void beforeHookedMethod(MethodHookParam methodHookParam) {
                    if (((String) methodHookParam.args[1]).equals("su")) {
                        methodHookParam.args[1] = "suxx";
                    }
                }
            });

            XposedHelpers.findAndHookMethod(Modifier.class, "isNative", Integer.TYPE, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam methodHookParam) {
                    methodHookParam.setResult(Boolean.valueOf(false));
                }
            });

            XposedHelpers.findAndHookMethod(System.class, "getProperty", String.class, new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void afterHookedMethod(MethodHookParam methodHookParam) {
                    if ("vxp".equals(methodHookParam.args[0])) {
                        methodHookParam.setResult(null);
                    }
                }
            });

            XposedHelpers.findAndHookMethod("android.os.SystemProperties", classLoader, "get", String.class, new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void afterHookedMethod(MethodHookParam methodHookParam) {
                    if ("ro.secure".equals(methodHookParam.args[0])) {
                        methodHookParam.setResult(0);
                    }
                }
            });

            XposedHelpers.findAndHookMethod(File.class, "list", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam methodHookParam) {
                    String[] strArr = (String[]) methodHookParam.getResult();
                    if (strArr != null) {
                        ArrayList arrayList = new ArrayList();
                        for (String str : strArr) {
                            if (!(str.toLowerCase().contains("xposed") || str.equals("su"))) {
                                arrayList.add(str);
                            }
                        }
                        methodHookParam.setResult(arrayList.toArray(new String[0]));
                    }
                }
            });

            XposedBridge.hookAllMethods(System.class, "getenv", new XC_MethodHook() {
                private String a(String str) {
                    int i = 0;
                    List asList = Arrays.asList(str.split(":"));
                    ArrayList arrayList = new ArrayList();
                    for (int i2 = 0; i2 < asList.size(); i2++) {
                        if (!((String) asList.get(i2)).toLowerCase().contains("xposed")) {
                            arrayList.add(asList.get(i2));
                        }
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    while (i < arrayList.size()) {
                        stringBuilder.append(arrayList);
                        if (i != arrayList.size() - 1) {
                            stringBuilder.append(":");
                        }
                        i++;
                    }
                    return stringBuilder.toString();
                }

                @Override
                protected void afterHookedMethod(MethodHookParam methodHookParam) {
                    if (methodHookParam.args.length == 0) {
                        methodHookParam.setResult(a((String) ((Map) methodHookParam.getResult()).get("CLASSPATH")));
                    } else if ("CLASSPATH".equals(methodHookParam.args[0])) {
                        methodHookParam.setResult(a((String) methodHookParam.getResult()));
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        //====================================
        Util.findAndHookMethod("android.app.ApplicationPackageManager", classLoader, "getInstalledPackages", Integer.TYPE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(RootAndXposedCheck2.packageFilter((List) methodHookParam.getResult()));
            }
        });
        Util.findAndHookMethod("android.app.ApplicationPackageManager", classLoader, "getPackagesHoldingPermissions", String[].class, Integer.TYPE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(RootAndXposedCheck2.packageFilter((List) methodHookParam.getResult()));
            }
        });
        Util.findAndHookMethod("android.app.ApplicationPackageManager", classLoader, "getPreferredPackages", Integer.TYPE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(RootAndXposedCheck2.packageFilter((List) methodHookParam.getResult()));
            }
        });
        Util.findAndHookMethod("android.app.ApplicationPackageManager", classLoader, "getInstalledApplications", Integer.TYPE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                methodHookParam.setResult(RootAndXposedCheck2.getApplicationInfo((List) methodHookParam.getResult()));
            }
        });
        Util.hookMethod(ActivityManager.class, "getRunningAppProcesses", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                List<ActivityManager.RunningAppProcessInfo> list = (List) methodHookParam.getResult();
                if (list != null) {
                    ArrayList arrayList = new ArrayList();
                    for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
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
                List<ActivityManager.RunningServiceInfo> list = (List) methodHookParam.getResult();
                if (list != null) {
                    ArrayList arrayList = new ArrayList();
                    for (ActivityManager.RunningServiceInfo runningServiceInfo : list) {
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

    private static boolean packageFilter(String str) {
        return TextUtils.isEmpty(str) ? false : str.contains("com.daxi.") || str.contains("com.jubo.") || str.contains("com.kingroot.") || str.contains("xposed") || str.contains("eu.chainfire.supersu");
    }

    private static List<ApplicationInfo> getApplicationInfo(List<ApplicationInfo> list) {
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

    private static List<PackageInfo> packageFilter(List<PackageInfo> list) {
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
