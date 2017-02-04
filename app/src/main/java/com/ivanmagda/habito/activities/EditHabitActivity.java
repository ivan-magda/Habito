package com.ivanmagda.habito.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ivanmagda.habito.R;
import com.ivanmagda.habito.model.ResetFrequency;
import com.ivanmagda.habito.pickers.TimePickerFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import petrov.kristiyan.colorpicker.ColorPicker;

public class EditHabitActivity extends AppCompatActivity implements TimePickerFragment.OnTimeSetListener {

    private static final String TAG = "EditHabitActivity";

    @BindView(R.id.tv_reminder_time)
    TextView reminderTimeTextView;

    @BindView(R.id.spinner_reset)
    Spinner resetFrequencySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configure();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_habit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                Snackbar.make(findViewById(R.id.activity_edit_habit), "Saved", Snackbar.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void configure() {
        setContentView(R.layout.activity_edit_habit);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_create_name);
        }

        List<String> resetFrequencies = Arrays.asList(ResetFrequency.ALL);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                resetFrequencies);
        resetFrequencySpinner.setAdapter(adapter);
        resetFrequencySpinner.setPrompt(getResources().getString(R.string.spinner_prompt));
        resetFrequencySpinner.setSelection(resetFrequencies.indexOf(ResetFrequency.NEVER));
        resetFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @OnClick(R.id.tv_reminder_time)
    void onDateSpinnerClick() {
        int defaultHour = 8;
        int defaultMin = 0;

//        if (modifiedHabit.hasReminder()) {
//            Reminder reminder = modifiedHabit.getReminder();
//            defaultHour = reminder.getHour();
//            defaultMin = reminder.getMinute();
//        }

        showTimePicker(defaultHour, defaultMin);
    }

    private void showTimePicker(int defaultHour, int defaultMin) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnTimeSetListener(this);
        timePickerFragment.show(getSupportFragmentManager(), "TimePicker");
    }

    @OnClick(R.id.bt_pick_color)
    void showColorPicker() {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.getPositiveButton().setTextColor(getResources().getColor(R.color.colorAccent));
        colorPicker.setRoundColorButton(true);
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                Log.d(TAG, "Choose color: " + color + " at position: " + position);
            }

            @Override
            public void onCancel() {
            }
        });
        colorPicker.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d(TAG, "Selected time: " + hourOfDay + " : " + minute);
        reminderTimeTextView.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
    }
}
