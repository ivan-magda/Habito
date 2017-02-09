package com.ivanmagda.habito.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.ivanmagda.habito.R;
import com.ivanmagda.habito.barchart.HabitoBarChartConfigurator;
import com.ivanmagda.habito.barchart.HabitoBarChartDataLoader;
import com.ivanmagda.habito.barchart.HabitoBarChartDataSource;
import com.ivanmagda.habito.barchart.HabitoBarChartRange;
import com.ivanmagda.habito.models.Habit;
import com.ivanmagda.habito.sync.FirebaseSyncUtils;
import com.ivanmagda.habito.view.model.HabitDetailViewModel;
import com.ivanmagda.habito.view.model.HabitoBarChartViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailHabitActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener,
        LoaderManager.LoaderCallbacks<HabitoBarChartDataSource> {

    public static final String HABIT_EXTRA_KEY = "com.ivanmagda.habito.activities.habit";

    private static final String TAG = "DetailHabitActivity";

    private static final int BAR_CHART_DATA_SOURCE_LOADER = 1;
    private static final int RC_EDIT_HABIT = 1234;

    @BindView(R.id.bar_chart)
    BarChart barChart;

    @BindView(R.id.tv_score)
    TextView scoreTextView;

    @BindView(R.id.sp_date_range)
    Spinner dateRangeSpinner;

    @BindView(R.id.tv_date_range)
    TextView dateRangeTextView;

    private Habit mHabit;
    private HabitoBarChartConfigurator mBarChartConfigurator;
    private HabitoBarChartRange.DateRange mBarChartRange = HabitoBarChartRange.DateRange.WEEK;
    private HabitDetailViewModel mViewModel = new HabitDetailViewModel();

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
            mHabit = data.getParcelableExtra(EditHabitActivity.EDIT_HABIT_RESULT);
            updateUI();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void configure() {
        setContentView(R.layout.activity_detail_habit);
        ButterKnife.bind(this);

        mBarChartConfigurator = new HabitoBarChartConfigurator(barChart);

        getHabitFromExtras();
        configureDateSpinner();
        updateUI();
    }

    private void updateUI() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mHabit.getRecord().getName());
        }

        String scoreString = mViewModel.getScoreString(mHabit.getRecord().getScore());
        scoreTextView.setText(scoreString);
        dateRangeTextView.setText(mViewModel.getDateRangeString());

        getSupportLoaderManager().restartLoader(BAR_CHART_DATA_SOURCE_LOADER, null, this);
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
            updateUI();
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

    private void configureDateSpinner() {
        List<String> dateRanges = HabitoBarChartRange.allStringValues(this);
        ArrayAdapter<String> resetAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                dateRanges);
        dateRangeSpinner.setAdapter(resetAdapter);
        dateRangeSpinner.setSelection(dateRanges.indexOf(mBarChartRange.stringValue(this)));
        dateRangeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();
        if (!selected.equals(mBarChartRange.stringValue(this))) {
            mBarChartRange = HabitoBarChartRange.DateRange.fromString(selected, this);
            mViewModel.setDateRange(mBarChartRange);
            updateUI();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public Loader<HabitoBarChartDataSource> onCreateLoader(int id, Bundle args) {
        return new HabitoBarChartDataLoader(this, mHabit, mBarChartRange);
    }

    @Override
    public void onLoadFinished(Loader<HabitoBarChartDataSource> loader,
                               HabitoBarChartDataSource dataSource) {
        HabitoBarChartViewModel viewModel = new HabitoBarChartViewModel(mHabit, mBarChartRange);
        mBarChartConfigurator.setup(dataSource, viewModel);
        barChart.animateY(1000);
    }

    @Override
    public void onLoaderReset(Loader<HabitoBarChartDataSource> loader) {
        barChart.clear();
    }

}
