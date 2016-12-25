package com.guowei.lv.app;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.guowei.lv.combineddatetimepickerdialog.lib.CombinedDateTimePickerDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and show the CombinedDateTimePickerDialog in a fragment
                DialogFragment dialogFragment = new DateTimePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "dateTimePickerFragment");
            }
        });
    }

    /**
     * The DialogFragment for hosting the CombinedDateTimePickerDialog
     */
    public static class DateTimePickerFragment extends DialogFragment
            implements CombinedDateTimePickerDialog.OnDateTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new CombinedDateTimePickerDialog(getActivity(), this);
        }

        @Override
        public void onDateTimeSet(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
            // Do something with the set date and time here
            Toast.makeText(this.getContext(), year + "-" + month + "-" + dayOfMonth + ", " + hourOfDay + ":" + minute, Toast.LENGTH_LONG).show();
        }
    }
}
