package com.guowei.lv.combineddatetimepickerdialog.lib;


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

/**
 * Custom dialog class to show both a date picker and a time picker at the same time
 */
public class CombinedDateTimePickerDialog extends AlertDialog implements DialogInterface.OnClickListener,
        TimePicker.OnTimeChangedListener, DateSlideAdaptor.OnItemClickedListener, DatePickerFragment.OnDateSetListener {

    private static final int DIALOG_HEIGHT = 600;
    private static final int DIALOG_WIDTH = 400;

    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";

    private static final String DATE_PICKER_FRAGMENT_TAG = "datePicker";

    private FragmentActivity activity;
    private TimePicker timePicker;
    private ViewPager dateSlide;
    private DateSlideAdaptor dateSlideAdaptor;

    private OnDateTimeSetListener onDateTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatePickerFragment dpf = (DatePickerFragment) activity.getSupportFragmentManager().findFragmentByTag(DATE_PICKER_FRAGMENT_TAG);
        if (dpf != null) {
            dpf.setListener(this);
        }
    }

    @Override
    public void onDateSlideItemClick(int year, int month, int day) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(this);
        fragment.show(activity.getSupportFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
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
        final View view = inflater.inflate(R.layout.lv_combined_dialog, null);

        setView(view);
        setButton(BUTTON_POSITIVE, themeContext.getString(R.string.lv_ok), this);
        setButton(BUTTON_NEGATIVE, themeContext.getString(R.string.lv_cancel), this);
        setupTimePicker(view);
        setupDateSlide(activity, view);
        setupResetButton(view);
        getWindow().setLayout(DIALOG_WIDTH, DIALOG_HEIGHT);
    }

    private void setupResetButton(View view) {
        ImageButton resetButton = (ImageButton) view.findViewById(R.id.reset_now);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
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
                        moveDateSlideToMiddle();
                    } else if (selectedPosition == DateSlideAdaptor.LAST_ITEM_INDEX) {
                        dateSlideAdaptor.moveNext();
                        moveDateSlideToMiddle();
                    }
                }
            }

        });
        dateSlideAdaptor = new DateSlideAdaptor(context, this);
        dateSlide.setAdapter(dateSlideAdaptor);
        dateSlide.setCurrentItem(1);

        ImageButton leftArrow = (ImageButton) view.findViewById(R.id.left_arrow);
        ImageButton rightArrow = (ImageButton) view.findViewById(R.id.right_arrow);

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

    private void moveDateSlideToMiddle() {
        dateSlide.setCurrentItem(DateSlideAdaptor.MIDDLE_ITEM_INDEX, false);
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
        state.putInt(KEY_YEAR, dateSlideAdaptor.getCurrentYear());
        state.putInt(KEY_MONTH, dateSlideAdaptor.getCurrentMonth());
        state.putInt(KEY_DAY, dateSlideAdaptor.getCurrentDay());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int year = savedInstanceState.getInt(KEY_YEAR);
        int month = savedInstanceState.getInt(KEY_MONTH);
        int day = savedInstanceState.getInt(KEY_DAY);
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
