package com.example.xposedhookreceivebigimgdemo;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    private static final String HOOKAPP = BuildConfig.APPLICATION_ID; //"com.tencent.mm"
    private static final String TAG = "MainHook";   //logt + Tab
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        /**
         * XposedBridge.log()：以原生logcat的形式写入到/data/user_de/0/de.robv.android.xposed.installer/log/error.log
         */
        if(loadPackageParam.packageName.equals(HOOKAPP)){//BuildConfig.APPLICATION_ID
            Class clazz = loadPackageParam.classLoader.loadClass("com.example.xposedhookreceivebigimgdemo.MainActivity");
            XposedHelpers.findAndHookMethod(clazz, "toastMessage", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("Hook成功！！");
                    param.setResult("已经被Hook掉了！！");
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("Hook已被拦截！");
                }
            });
        }

    }

    private static String downloadWxPicRes(ClassLoader classLoader, long msgSvrId, long msgId, String imgPath) {
        Class imageHDDownloadAndSaveMgrClazz = XposedHelpers.findClass("com.tencent.mm.ui.chatting.gallery.l", classLoader);
        Object imageHDDownloadAndSaveMgrObj = XposedHelpers.callStaticMethod(imageHDDownloadAndSaveMgrClazz,"dGI"); //getImageHDDownloadAndSaveMgr
        Class oClazz = XposedHelpers.findClass("com.tencent.mm.at.o", classLoader);
        Object downloadImgServiceObj = XposedHelpers.callStaticMethod(oClazz, "ahm");
        Class oClazz2 = XposedHelpers.findClass("com.tencent.mm.at.o", classLoader);
        Object imgInfoStorageObj = XposedHelpers.callStaticMethod(oClazz2, "ahl");
        Object imgfoObj = XposedHelpers.callMethod(imgInfoStorageObj, "fI", msgSvrId);  //e_Imgfo getImgfoByMsgSvrId_fI(long arg10)
        long id = XposedHelpers.getLongField(imgfoObj, "fDy");

        XposedHelpers.callMethod(
                downloadImgServiceObj, "a",
                Long.valueOf(id),
                msgId,
                Integer.valueOf(0),
                Integer.valueOf(10000),
                Integer.valueOf(2130837945),
                imageHDDownloadAndSaveMgrObj,
                Integer.valueOf(0),
                Boolean.valueOf(true)
        );
//        Class clazz4 = XposedHelpers.findClass("ahm", classLoader);
        Class oClazz3 = XposedHelpers.findClass("com.tencent.mm.at.o", classLoader);
        Object imgInfoStorageObj2 = XposedHelpers.callStaticMethod(oClazz3, "ahl");   //public static g_ImgInfoStorage getImgInfoStorage__ahl()

        String imgDownloadPath = XposedHelpers.callMethod(imgInfoStorageObj2, "I", imgPath, Boolean.valueOf(true)).toString();  //public final String I(String arg7, boolean arg8)
        Log.i(TAG, "imgDownload:" + imgDownloadPath);
        return imgDownloadPath;
    }
}
