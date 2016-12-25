package com.guowei.lv.combineddatetimepickerdialog.lib;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class DateTimeUtils {

    static String formatCurrentTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy");
        return format.format(date);
    }

    static boolean isToday(Date date) {
        return DateUtils.isToday(date.getTime());
    }

    static boolean isTomorrow(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return DateUtils.isToday(calendar.getTime().getTime());
    }

    static boolean isYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return DateUtils.isToday(calendar.getTime().getTime());
    }
}
