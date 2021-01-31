package com.lagou.utils;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/29 23:07
 */
@Component
public class TimeUtils {
    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -3);

        return FastDateFormat.getInstance("yyyyMMdd").format(calendar.getTime());
    }
}
