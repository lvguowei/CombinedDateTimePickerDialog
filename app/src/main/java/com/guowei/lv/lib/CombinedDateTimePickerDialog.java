package com.guowei.lv.lib;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.util.Calendar;

public class CombinedDateTimePickerDialog extends AlertDialog implements DialogInterface.OnClickListener,
        TimePicker.OnTimeChangedListener, DateSlideAdaptor.OnItemClickedListener, DatePickerFragment.OnDateSetListener {

    private static final int DIALOG_HEIGHT = 600;
    private static final int DIALOG_WIDTH = 400;

    private FragmentActivity activity;
    private TimePicker timePicker;
    private ViewPager dateSlide;
    private DateSlideAdaptor dateSlideAdaptor;
    private ImageButton leftArrow;
    private ImageButton rightArrow;
    private ImageButton resetButton;

    private OnDateTimeSetListener onDateTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatePickerFragment dpf = (DatePickerFragment) activity.getSupportFragmentManager().findFragmentByTag("datePicker");
        if (dpf != null) {
            dpf.setListener(this);
        }
    }

    @Override
    public void onDateSlideItemClick(int year, int month, int day) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(this);
        fragment.show(activity.getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(int year, int month, int dayOfMonth) {
        dateSlideAdaptor.updateDate(year, month, dayOfMonth);
        dateSlide.setCurrentItem(1);
    }

    public interface OnDateTimeSetListener {
        void onDateTimeSet(int year, int month, int dayOfMonth, int hourOfDay, int minute);
    }


    public CombinedDateTimePickerDialog(FragmentActivity activity,
                                        OnDateTimeSetListener listener) {
        super(activity, 0);

        this.activity = activity;
        this.onDateTimeSetListener = listener;

        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.combined_dialog, null);

        setView(view);
        setButton(BUTTON_POSITIVE, themeContext.getString(R.string.ok), this);
        setButton(BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this);
        setupTimePicker(view);
        setupDateSlide(activity, view);
        setupResetButton(view);
        getWindow().setLayout(DIALOG_WIDTH, DIALOG_HEIGHT);
    }

    private void setupResetButton(View view) {
        resetButton = (ImageButton) view.findViewById(R.id.reset_now);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                timePicker.setIs24HourView(DateFormat.is24HourFormat(activity));
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);

                dateSlideAdaptor.updateDate(year, month, dayOfMonth);
                dateSlide.setCurrentItem(1);
            }
        });
    }

    private void setupDateSlide(final Context context, View view) {
        dateSlide = (ViewPager) view.findViewById(R.id.dateSlide);
        dateSlide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int selectedPosition = 1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // no op
            }

            @Override
            public void onPageSelected(int position) {
                this.selectedPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (selectedPosition == DateSlideAdaptor.FIRST_ITEM_INDEX) {
                        dateSlideAdaptor.movePrev();
                        dateSlide.setCurrentItem(DateSlideAdaptor.MIDDLE_ITEM_INDEX, false);
                    } else if (selectedPosition == DateSlideAdaptor.LAST_ITEM_INDEX) {
                        dateSlideAdaptor.moveNext();
                        dateSlide.setCurrentItem(DateSlideAdaptor.MIDDLE_ITEM_INDEX, false);
                    }
                }
            }

        });
        dateSlideAdaptor = new DateSlideAdaptor(context, this);
        dateSlide.setAdapter(dateSlideAdaptor);
        dateSlide.setCurrentItem(1);

        leftArrow = (ImageButton) view.findViewById(R.id.left_arrow);
        rightArrow = (ImageButton) view.findViewById(R.id.right_arrow);

        leftArrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dateSlide.setCurrentItem(dateSlide.getCurrentItem() - 1);
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dateSlide.setCurrentItem(dateSlide.getCurrentItem() + 1);
            }
        });
    }

    private void setupTimePicker(View view) {
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        timePicker.setIs24HourView(DateFormat.is24HourFormat(activity));
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setOnTimeChangedListener(this);
    }

    @NonNull
    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt("year", dateSlideAdaptor.getCurrentYear());
        state.putInt("month", dateSlideAdaptor.getCurrentMonth());
        state.putInt("day", dateSlideAdaptor.getCurrentDay());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int year = savedInstanceState.getInt("year");
        int month = savedInstanceState.getInt("month");
        int day = savedInstanceState.getInt("day");

        dateSlideAdaptor.updateDate(year, month, day);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (onDateTimeSetListener != null) {

                    onDateTimeSetListener.onDateTimeSet(dateSlideAdaptor.getCurrentYear(),
                            dateSlideAdaptor.getCurrentMonth(),
                            dateSlideAdaptor.getCurrentDay(),
                            timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute());
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        // no op
    }
}
