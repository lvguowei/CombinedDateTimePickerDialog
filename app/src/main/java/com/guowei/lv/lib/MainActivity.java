package com.guowei.lv.lib;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "tag";
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DateTimePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "dateTimePickerFragment");
            }
        });

    }

    public static class DateTimePickerFragment extends DialogFragment
            implements CombinedDateTimePickerDialog.OnDateTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new CombinedDateTimePickerDialog(getActivity(), this);
        }

        @Override
        public void onDateTimeSet(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
            Toast.makeText(this.getContext(), year + "-" + month + "-" + dayOfMonth + ", " + hourOfDay + ":" + minute, Toast.LENGTH_LONG).show();
        }
    }
}
