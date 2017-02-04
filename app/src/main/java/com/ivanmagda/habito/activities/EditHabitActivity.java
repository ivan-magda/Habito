package com.ivanmagda.habito.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ivanmagda.habito.R;
import com.ivanmagda.habito.models.HabitRecord;
import com.ivanmagda.habito.models.ReminderTime;
import com.ivanmagda.habito.models.ResetFrequency;
import com.ivanmagda.habito.pickers.TimePickerFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import petrov.kristiyan.colorpicker.ColorPicker;

public class EditHabitActivity extends AppCompatActivity implements TimePickerFragment.OnTimeSetListener {

    private static final String TAG = "EditHabitActivity";

    @BindView(R.id.et_habit_name)
    EditText nameEditText;

    @BindView(R.id.spinner_reset)
    Spinner resetFrequencySpinner;

    @BindView(R.id.et_habit_target)
    EditText targetEditText;

    @BindView(R.id.tv_reminder_time)
    TextView reminderTimeTextView;

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mHabitsDatabaseReference;

    private int mSelectedColor = Color.WHITE;
    private ResetFrequency mResetFrequency = new ResetFrequency();
    private ReminderTime mReminderTime = new ReminderTime();

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
                createNew();
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

        initializeFirebase();

        List<String> resetFrequencies = Arrays.asList(ResetFrequency.ALL);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                resetFrequencies);
        resetFrequencySpinner.setAdapter(adapter);
        resetFrequencySpinner.setPrompt(getResources().getString(R.string.spinner_prompt));
        resetFrequencySpinner.setSelection(resetFrequencies.indexOf(mResetFrequency.getTypeName()));
        resetFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                mResetFrequency.setType(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initializeFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mHabitsDatabaseReference = mFirebaseDatabase.getReference().child("habits");
    }

    @OnClick(R.id.tv_reminder_time)
    void onDateSpinnerClick() {
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
                mSelectedColor = color;
                nameEditText.setTextColor(color);
            }

            @Override
            public void onCancel() {
            }
        });
        colorPicker.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mReminderTime.setHour(hourOfDay);
        mReminderTime.setMinutes(minute);
        reminderTimeTextView.setText(mReminderTime.getTimeString());
    }

    private void createNew() {
        checkInput();

        long now = System.currentTimeMillis();
        String userId = "Kc1E6kPynflmh34hvmJ";
        String name = nameEditText.getText().toString().trim();

        String targetString = targetEditText.getText().toString().trim();
        int target = 0;
        if (!TextUtils.isEmpty(targetString)) {
            try {
                target = Integer.parseInt(targetString);
            } catch (NumberFormatException e) {
                Log.d(TAG, "Failed to parse target value");
            }
        }
        int score = 0;

        HabitRecord habit = new HabitRecord(userId, now, name, mSelectedColor, target,
                mResetFrequency.getTypeName(), now, mReminderTime.getHour(),
                mReminderTime.getMinutes(), score, null);
        mHabitsDatabaseReference.push().setValue(habit);
        onBackPressed();
    }

    private void checkInput() {
        String name = nameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, R.string.toast_empty_name, Toast.LENGTH_SHORT).show();
        }
    }

}
