package com.ivanmagda.habito.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ivanmagda.habito.R;
import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.sync.FirebaseSyncUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailHabitActivity extends AppCompatActivity {

    public static final String HABIT_EXTRA_KEY = "com.ivanmagda.habito.activities.habit";

    private static final String TAG = "DetailHabitActivity";
    private static final int RC_EDIT_HABIT = 1234;

    @BindView(R.id.tv_score)
    TextView scoreTextView;

    private Habit mHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configure();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_habit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit:
                editHabit();
                return true;
            case R.id.action_delete:
                delete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_EDIT_HABIT && resultCode == RESULT_OK) {
            Habit changedHabit = data.getParcelableExtra(EditHabitActivity.EDIT_HABIT_RESULT);
            Log.d(TAG, "Changed habit: " + changedHabit);
            Toast.makeText(this, "Result OK!", Toast.LENGTH_SHORT).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void configure() {
        setContentView(R.layout.activity_detail_habit);
        ButterKnife.bind(this);

        getHabitFromExtras();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mHabit.getRecord().getName());
        }

        updateScoreText();
    }

    private void getHabitFromExtras() {
        Intent intent = getIntent();
        if (intent.hasExtra(HABIT_EXTRA_KEY)) {
            mHabit = intent.getParcelableExtra(HABIT_EXTRA_KEY);
        } else {
            throw new IllegalArgumentException("Put habit in the intent extras to be able to see details");
        }
    }

    private void editHabit() {
        Intent intent = new Intent(this, EditHabitActivity.class);
        intent.putExtra(EditHabitActivity.EDIT_HABIT_EXTRA_KEY, mHabit);
        startActivityForResult(intent, RC_EDIT_HABIT);
    }

    private void updateScoreText() {
        String scoreString = String.valueOf(mHabit.getRecord().getScore());
        scoreTextView.setText(scoreString);
    }

    @OnClick(R.id.bt_increase)
    void onIncreaseScoreClick() {
        final int oldScore = mHabit.getRecord().getScore();
        mHabit.increaseScore();
        updateScoreIfNeeded(oldScore);
    }

    @OnClick(R.id.bt_decrease)
    void onDecreaseClick() {
        final int oldScore = mHabit.getRecord().getScore();
        mHabit.decreaseScore();
        updateScoreIfNeeded(oldScore);
    }

    private void updateScoreIfNeeded(int oldValue) {
        if (oldValue != mHabit.getRecord().getScore()) {
            updateScoreText();
            FirebaseSyncUtils.applyChangesForHabit(mHabit);
        }
    }

    private void delete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.action_delete)
                .setMessage(R.string.delete_habit_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseSyncUtils.deleteHabit(mHabit);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

}
