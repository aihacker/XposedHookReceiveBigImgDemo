package com.example.xposedhookreceivebigimgdemo.utils;

import java.util.Calendar;

/**
 * @author glsite.com
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class Tool {
    public static String gettime() {
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH) + 1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //秒
        int second = calendar.get(Calendar.SECOND);
        String time="激活:" + year + "年" + month + "月" + day + "日" + hour + ":" + minute + ":" + second;
        //  Acesslog(time);
        return time;


    }
}
