package com.example.xposedhookreceivebigimgdemo.hook;

import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

/**
 * @author glsite.com
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class OpenWxLog {

    private static final String TAG = "OpenWxLog";

    public OpenWxLog(ClassLoader classLoader1) {
        //isLogcatOpen
        XposedHelpers.findAndHookMethod("com.tencent.mm.xlog.app.XLogSetup",classLoader1, "keep_setupXLog",
        boolean.class, String.class, String.class, Integer.class, Boolean.class,Boolean.class,String.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[5] = true;
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[5] = true;
                        super.afterHookedMethod(param);
                        Log.i(TAG,"keep_setupXLog参数isLogcatOpen: " +param.args[5]);
                    }
                });

    }
}
