package com.niucai.test_createsayshares.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Qi on 2017/10/10.
 * 时间戳转换为时间
 */

public class DateUtils {

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *
     * @param time
     * @return
     */
    public static String times(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm");
        @SuppressWarnings("unused")
//        long lcc = Long.valueOf(time);
//        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(time * 1000L));
        return times;

    }
}
