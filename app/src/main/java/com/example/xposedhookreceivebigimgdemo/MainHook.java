package com.example.xposedhookreceivebigimgdemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.xposedhookreceivebigimgdemo.hook.CloseXposedCheck;
import com.example.xposedhookreceivebigimgdemo.hook.Crack.utils.DBHook;
import com.example.xposedhookreceivebigimgdemo.hook.OpenWxLog;
import com.example.xposedhookreceivebigimgdemo.utils.Tool;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class MainHook implements IXposedHookLoadPackage {
//    private static final String HOOKAPP = BuildConfig.APPLICATION_ID; //"com.tencent.mm"
    private static final String TAG = "MainHook";   //logt
    public static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";

    public static Context Sendcontext;
    public static ClassLoader classLoader;


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {


    }

    private void starWxHook(XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals(WECHAT_PACKAGE_NAME) || WECHAT_PACKAGE_NAME.contains(lpparam.processName)) //|| !WECHAT_PACKAGE_NAME.equals(lpparam.processName))//
        {

            findAndHookMethod(Application.class, "attach",
                    Context.class, new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.v(TAG, "afterHookedMethod 进入微信进程 " + Tool.gettime());
                            initHOOK(param);
                        }


                    });
        }

    }

    private void initHOOK(XC_MethodHook.MethodHookParam param) throws InterruptedException {
        Log.v(TAG, "初始化   " );
        Context context = (Context) param.args[0];
        ClassLoader classLoader1 = ((Context) param.args[0]).getClassLoader();
        Sendcontext = context;
        classLoader = classLoader1;


/*        new LauncherUI(classLoader1);
        //new HCEService(classLoader1);
        new ProcessService(context, classLoader1);
        //new CoreService(classLoader1);

        new HookSnsTimeLineUI(classLoader1);
        new HookRecvMessage(classLoader1);
        new HookSnsMessage(classLoader1);
        new HookIMSend(classLoader1);
        new Redenvelopes(classLoader1);
        new DBHook(classLoader1);

        new ChatroomInfoUI(classLoader1);
        new SelectContactUI(classLoader1);
        new RoomCardUI(classLoader1);
        new ModRemarkRoomNameUI(classLoader1);
        new ContactInfoUI(classLoader1);
        //hook();
        //     new IntentWework(classLoader1);
        // new Latino(classLoader1);
        //  new WxLog(classLoader1);*/
        new OpenWxLog(classLoader1);
        new CloseXposedCheck(classLoader1);
        new DBHook(classLoader1);
    }


    //    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
    //        // new debuggable().initZygote(startupParam);
    //       // XposedBridge.hookAllMethods(ActivityThread.class, "systemMain", new SystemServiceHook());
    //
    //    }

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

    public void hookText(XC_LoadPackage.LoadPackageParam loadPackageParam){
        /**
         * XposedBridge.log()：以原生logcat的形式写入到/data/user_de/0/de.robv.android.xposed.installer/log/error.log
         */
        if(loadPackageParam.packageName.equals("com.tencent.mm")){//BuildConfig.APPLICATION_ID
            Class clazz = null;
            try {
                clazz = loadPackageParam.classLoader.loadClass("com.example.xposedhookreceivebigimgdemo.MainActivity");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            findAndHookMethod(clazz, "toastMessage", new XC_MethodHook() {
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
}
