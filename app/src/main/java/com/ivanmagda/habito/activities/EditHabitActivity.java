package com.ivanmagda.habito.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ivanmagda.habito.R;
import com.ivanmagda.habito.analytics.HabitoAnalytics;
import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.models.HabitRecord;
import com.ivanmagda.habito.models.ReminderTime;
import com.ivanmagda.habito.models.ResetFrequency;
import com.ivanmagda.habito.pickers.TimePickerFragment;
import com.ivanmagda.habito.sync.FirebaseSyncUtils;
import com.ivanmagda.habito.utils.HabitoScoreUtils;
import com.ivanmagda.habito.utils.ReminderUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import petrov.kristiyan.colorpicker.ColorPicker;

public class EditHabitActivity extends AppCompatActivity implements TimePickerFragment.OnTimeSetListener {

    public static final String EDIT_HABIT_RESULT = "com.ivanmagda.habito.activities.edit_result";
    public static final String EDIT_HABIT_EXTRA_KEY = "com.ivanmagda.habito.activities.edit";

    private static final String TAG = "EditHabitActivity";

    @BindView(R.id.et_habit_name)
    EditText nameEditText;

    @BindView(R.id.spinner_reset)
    Spinner resetFrequencySpinner;

    @BindView(R.id.et_habit_target)
    EditText targetEditText;

    @BindView(R.id.tv_reminder_time)
    TextView reminderTimeTextView;

    /**
     * The original habit.
     * If original habit is not null, then we are in editing mode, otherwise creating new.
     */
    private Habit mOriginalHabit;
    private Habit mEditingHabit;

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
                setResult(RESULT_CANCELED);
                onBackPressed();
                return true;
            case R.id.action_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void configure() {
        setContentView(R.layout.activity_edit_habit);

        ButterKnife.bind(this);
        getExtras();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            int titleId = (mOriginalHabit == null ? R.string.activity_create_label
                    : R.string.activity_edit_label);
            actionBar.setTitle(titleId);
        }

        HabitRecord record = mEditingHabit.getRecord();
        nameEditText.setText(record.getName());
        if (record.getColor() != HabitRecord.DEFAULT_COLOR) {
            nameEditText.setTextColor(record.getColor());
        }

        targetEditText.setText(String.valueOf(record.getTarget()));
        updateTimeText();

        List<String> resetFrequencies = Arrays.asList(ResetFrequency.ALL);
        ArrayAdapter<String> resetAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                resetFrequencies);
        resetFrequencySpinner.setAdapter(resetAdapter);
        resetFrequencySpinner.setPrompt(getResources().getString(R.string.spinner_prompt));
        String selection = (mOriginalHabit == null ? ResetFrequency.NEVER
                : mOriginalHabit.getRecord().getResetFreq());
        resetFrequencySpinner.setSelection(resetFrequencies.indexOf(selection));
        resetFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                mEditingHabit.getRecord().setResetFreq(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getExtras() {
        Intent intent = getIntent();
        if (intent.hasExtra(EDIT_HABIT_EXTRA_KEY)) {
            mOriginalHabit = intent.getParcelableExtra(EDIT_HABIT_EXTRA_KEY);
            mEditingHabit = mOriginalHabit.copy();
        } else {
            mOriginalHabit = null;
            mEditingHabit = new Habit();
        }
    }

    @OnClick(R.id.tv_reminder_time)
    void onDateSpinnerClick() {
        TimePickerFragment timePickerFragment;
        if (mEditingHabit.isReminderOn()) {
            HabitRecord record = mEditingHabit.getRecord();
            timePickerFragment = TimePickerFragment.newInstance(record.getReminderHour(),
                    record.getReminderMin());
        } else {
            timePickerFragment = new TimePickerFragment();
        }
        timePickerFragment.setOnTimeSetListener(this);
        timePickerFragment.show(getSupportFragmentManager(), "TimePicker");
    }

    @OnClick(R.id.bt_pick_color)
    void showColorPicker() {
        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setDefaultColorButton(mEditingHabit.getRecord().getColor());
        colorPicker.getPositiveButton().setTextColor(getResources().getColor(R.color.colorAccent));
        colorPicker.setRoundColorButton(true);
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                mEditingHabit.getRecord().setColor(color);
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
        mEditingHabit.getRecord().setReminderHour(hourOfDay);
        mEditingHabit.getRecord().setReminderMin(minute);
        updateTimeText();
    }

    @Override
    public void onCancel() {
        mEditingHabit.getRecord().setReminderHour(HabitRecord.REMINDER_OFF);
        mEditingHabit.getRecord().setReminderMin(HabitRecord.REMINDER_OFF);
        updateTimeText();
    }

    private void updateTimeText() {
        if (mEditingHabit.isReminderOn()) {
            HabitRecord record = mEditingHabit.getRecord();
            reminderTimeTextView.setText(ReminderTime.getTimeString(record.getReminderHour(),
                    record.getReminderMin()));
        } else {
            reminderTimeTextView.setText(R.string.off);
        }
    }

    private void save() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || !isInputCorrect()) return;

        putChanges();

        if (mOriginalHabit == null) {
            createNew();
        } else {
            applyChanges();
        }
    }

    private void createNew() {
        HabitoAnalytics.logCreateHabitWithName(mEditingHabit.getRecord().getName());
        FirebaseSyncUtils.createNewHabitRecord(mEditingHabit.getRecord());
        onBackPressed();
    }

    private void applyChanges() {
        Intent data = new Intent();
        data.putExtra(EDIT_HABIT_RESULT, mEditingHabit);
        setResult(RESULT_OK, data);

        ReminderUtils.processOn(mEditingHabit, this);
        HabitoScoreUtils.resetScore(mEditingHabit);
        FirebaseSyncUtils.applyChangesForHabit(mEditingHabit);

        finish();
    }

    private void putChanges() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        long now = System.currentTimeMillis();

        HabitRecord record = mEditingHabit.getRecord();
        if (mOriginalHabit == null) {
            record.setCreatedAt(now);
            record.setResetTimestamp(now);
        }
        record.setUserId(currentUser.getUid());
        record.setName(nameEditText.getText().toString().trim());
    }

    private boolean isInputCorrect() {
        String name = nameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, R.string.toast_empty_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        String targetString = targetEditText.getText().toString().trim();
        if (TextUtils.isEmpty(targetString)) {
            Toast.makeText(this, R.string.toast_target_empty, Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            mEditingHabit.getRecord().setTarget(Integer.parseInt(targetString));
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.toast_failed_target_value, Toast.LENGTH_LONG).show();
            targetEditText.requestFocus();
            return false;
        }

        return true;
    }

}
