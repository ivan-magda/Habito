package com.ivanmagda.habito.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ivanmagda.habito.R
import com.ivanmagda.habito.barchart.HabitoBarChartConfigurator
import com.ivanmagda.habito.barchart.HabitoBarChartDataLoader
import com.ivanmagda.habito.barchart.HabitoBarChartDataSource
import com.ivanmagda.habito.barchart.HabitoBarChartRange
import com.ivanmagda.habito.model.Habit
import com.ivanmagda.habito.sync.FirebaseSyncUtils
import com.ivanmagda.habito.view.model.HabitDetailViewModel
import com.ivanmagda.habito.view.model.HabitoBarChartViewModel
import kotlinx.android.synthetic.main.activity_detail_habit.*

class DetailHabitActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
        LoaderManager.LoaderCallbacks<HabitoBarChartDataSource> {

    private lateinit var habit: Habit
    private lateinit var barChartConfigurator: HabitoBarChartConfigurator
    private var barChartRange = HabitoBarChartRange.DateRange.WEEK
    private val viewModel = HabitDetailViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configure()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail_habit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_edit -> {
                editHabit()
                true
            }
            R.id.action_delete -> {
                delete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RC_EDIT_HABIT && resultCode == Activity.RESULT_OK) {
            habit = data.getParcelableExtra(EditHabitActivity.EDIT_HABIT_RESULT)
            updateUI()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun configure() {
        setContentView(R.layout.activity_detail_habit)

        barChartConfigurator = HabitoBarChartConfigurator(bar_chart)

        readExtras()
        configureDateSpinner()
        updateUI()

        bt_increase.setOnClickListener { onIncreaseScoreClick() }
        bt_decrease.setOnClickListener { onDecreaseScoreClick() }
    }

    private fun updateUI() {
        supportActionBar?.title = habit.record.name

        tv_score.text = viewModel.getScoreString(habit.record.score)
        tv_date_range.text = viewModel.dateRangeString

        supportLoaderManager.restartLoader(BAR_CHART_DATA_SOURCE_LOADER, Bundle(), this)
    }

    private fun readExtras() {
        if (intent.hasExtra(HABIT_EXTRA_KEY)) {
            habit = intent.getParcelableExtra(HABIT_EXTRA_KEY)
        } else {
            throw IllegalArgumentException("Put habit in the intent extras to be able to see details")
        }
    }

    private fun editHabit() {
        val intent = Intent(this, EditHabitActivity::class.java)
        intent.putExtra(EditHabitActivity.EDIT_HABIT_EXTRA_KEY, habit)
        startActivityForResult(intent, RC_EDIT_HABIT)
    }

    private fun onIncreaseScoreClick() {
        val oldScore = habit.record.score
        habit.increaseScore()
        updateScoreIfNeeded(oldScore)
    }

    private fun onDecreaseScoreClick() {
        val oldScore = habit.record.score
        habit.decreaseScore()
        updateScoreIfNeeded(oldScore)
    }

    private fun updateScoreIfNeeded(oldValue: Int) {
        if (oldValue != habit.record.score) {
            updateUI()
            FirebaseSyncUtils.applyChangesForHabit(habit)
        }
    }

    private fun delete() {
        AlertDialog.Builder(this)
                .setTitle(R.string.action_delete)
                .setMessage(R.string.delete_habit_message)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    FirebaseSyncUtils.deleteHabit(habit)
                    finish()
                }
                .setNegativeButton(android.R.string.no, null)
                .show()
    }

    private fun configureDateSpinner() {
        val dateRanges = HabitoBarChartRange.allStringValues(this)
        val resetAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                dateRanges)
        sp_date_range.adapter = resetAdapter
        sp_date_range.setSelection(dateRanges.indexOf(barChartRange.stringValue(this)))
        sp_date_range.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val selected = parent.getItemAtPosition(position).toString()
        if (selected != barChartRange.stringValue(this)) {
            barChartRange = HabitoBarChartRange.DateRange.fromString(selected, this)
            viewModel.dateRange = barChartRange
            updateUI()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}

    override fun onCreateLoader(id: Int, args: Bundle): Loader<HabitoBarChartDataSource> {
        return HabitoBarChartDataLoader(this, habit, barChartRange)
    }

    override fun onLoadFinished(loader: Loader<HabitoBarChartDataSource>,
                                dataSource: HabitoBarChartDataSource) {
        val viewModel = HabitoBarChartViewModel(barChartRange)
        barChartConfigurator.setup(dataSource, viewModel)
        bar_chart.animateY(1000)
    }

    override fun onLoaderReset(loader: Loader<HabitoBarChartDataSource>) {
        bar_chart.clear()
    }

    companion object {

        val HABIT_EXTRA_KEY = "com.ivanmagda.habito.activities.habit"

        private val BAR_CHART_DATA_SOURCE_LOADER = 1
        private val RC_EDIT_HABIT = 1234
    }
}
