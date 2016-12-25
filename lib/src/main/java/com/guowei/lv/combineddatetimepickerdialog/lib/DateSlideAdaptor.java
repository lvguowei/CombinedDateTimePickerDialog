package com.guowei.lv.combineddatetimepickerdialog.lib;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class DateSlideAdaptor extends PagerAdapter {

    static final int FIRST_ITEM_INDEX = 0;
    static final int MIDDLE_ITEM_INDEX = 1;
    static final int LAST_ITEM_INDEX = 2;

    private Context context;

    private List<Date> dates;

    private OnItemClickedListener listener;

    void updateDate(int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date newDate = c.getTime();

        c.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = c.getTime();

        c.add(Calendar.DAY_OF_MONTH, -2);
        Date yesterday = c.getTime();

        dates.clear();
        dates.add(yesterday);
        dates.add(newDate);
        dates.add(tomorrow);
        notifyDataSetChanged();
    }

    interface OnItemClickedListener {
        void onDateSlideItemClick(int year, int month, int day);
    }

    DateSlideAdaptor(Context context, OnItemClickedListener listener) {
        this.context = context;
        this.listener = listener;
        this.dates = new ArrayList<>();
        this.dates.addAll(getInitialDates());
    }

    private List<Date> getInitialDates() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        Date tomorrow = calendar.getTime();
        return Arrays.asList(yesterday, today, tomorrow);
    }

    private Date getCurrentDate() {
        return dates.get(MIDDLE_ITEM_INDEX);
    }

    int getCurrentYear() {
        Date d = getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.YEAR);

    }

    int getCurrentMonth() {
        Date d = getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);

        return calendar.get(Calendar.MONTH);

    }

    int getCurrentDay() {
        Date d = getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    void moveNext() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dates.get(LAST_ITEM_INDEX));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date newDate = calendar.getTime();
        dates.remove(FIRST_ITEM_INDEX);
        dates.add(newDate);
        notifyDataSetChanged();
    }

    void movePrev() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dates.get(0));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date newDate = calendar.getTime();
        dates.remove(LAST_ITEM_INDEX);
        dates.add(FIRST_ITEM_INDEX, newDate);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Date date = dates.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        TextView tv = (TextView) inflater.inflate(R.layout.lv_date_slide_view, container, false);
        if (DateTimeUtils.isToday(date)) {
            tv.setText(R.string.lv_today);
        } else if (DateTimeUtils.isTomorrow(date)) {
            tv.setText(R.string.lv_tomorrow);
        } else if (DateTimeUtils.isYesterday(date)) {
            tv.setText(R.string.lv_yesterday);
        } else {
            tv.setText(DateTimeUtils.formatCurrentTime(date));
        }

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDateSlideItemClick(getCurrentYear(), getCurrentMonth(), getCurrentDay());
            }
        });

        container.addView(tv);
        return tv;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
