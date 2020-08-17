package com.example.xposedhookreceivebigimgdemo.hook.Crack.utils;

import android.content.ContentValues;
import android.util.Log;

import com.example.xposedhookreceivebigimgdemo.utils.JsonUtil;

import de.robv.android.xposed.XC_MethodHook;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class DBHook {
    private static final String TAG = "DBHook: ";
//    private static Long msgId_WxFileIndex2 = 0L;
//    private static int msgType = -1;

    public DBHook(ClassLoader classLoader) {
        // /data/data/com.tencent.mm/MicroMsg/18b1982f9f317f178c6c50d065e33ffa/EnMicroMsg.db
        findAndHookMethod(findClass("com.tencent.wcdb.database.SQLiteDatabase", classLoader), "getPath",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        //    WxHook(TAG + "----getPath----" + " getResult :" + param.getResult());
                        String getPath = String.valueOf(param.getResult());
                        if (getPath.contains("EnMicroMsg.db")) {
                        }

                    }
                });
        //insertWithOnConflict(tableName, msgId, contentValues, int)
        findAndHookMethod("com.tencent.wcdb.database.SQLiteDatabase", classLoader,
                "insertWithOnConflict",
                String.class, String.class, ContentValues.class, int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Log.i(TAG ,"SQLiteDatabase -> insertWithOnConflict(): beforeHookedMethod");
                        String tableName = param.args[0].toString();
                        Log.i(TAG ,":插入表操作 [" + tableName +"]");
                        for (int i = 0; i < param.args.length; i++) {
                            Log.i(TAG ,"\t参数[" + i +"]: "+ JsonUtil.tojson(param.args[i]));
                        }
                        Log.i(TAG ,"=================================================================================\n");
//                        deaLInsertDB(param);
                        ContentValues contentValues = (ContentValues) param.args[2];
                        String p1 = param.args[1].toString();
                        Log.i(TAG ,"hacker -> 进入if前");
                        if ("WxFileIndex2".equals(tableName) && p1 == "rowid") {
                            Log.i(TAG ,"hacker -> 进入if了");
                            int msgType = contentValues.getAsInteger("msgType");
                            Log.i(TAG ,"hacker -> msgType="+msgType);
                            String talker = contentValues.getAsString("username");
                            Log.i(TAG ,"hacker -> talker="+talker);
                            long msgId_WxFileIndex2 = contentValues.getAsLong("msgId");
                            Log.i(TAG ,"hacker -> msgId_WxFileIndex2="+ msgId_WxFileIndex2);
                            String sqlStr = "select * from message where msgId = " + msgId_WxFileIndex2;
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Log.i(TAG ,"SQLiteDatabase -> insertWithOnConflict(): afterHookedMethod");
                    }
                });
    }

}
