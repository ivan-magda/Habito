package com.ivanmagda.habito.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ivanmagda.habito.R
import com.ivanmagda.habito.analytics.HabitoAnalytics
import com.ivanmagda.habito.model.Habit
import com.ivanmagda.habito.model.HabitRecord
import com.ivanmagda.habito.model.ReminderTime
import com.ivanmagda.habito.model.ResetFrequency
import com.ivanmagda.habito.picker.TimePickerFragment
import com.ivanmagda.habito.sync.FirebaseSyncUtils
import com.ivanmagda.habito.utils.HabitoScoreUtils
import com.ivanmagda.habito.utils.ReminderUtils
import kotlinx.android.synthetic.main.activity_edit_habit.*
import petrov.kristiyan.colorpicker.ColorPicker
import java.util.*

class EditHabitActivity : AppCompatActivity(), TimePickerFragment.OnTimeSetListener {

    /**
     * The original habit.
     * If original habit is not null, then we are in editing mode, otherwise creating new.
     */
    private var originalHabit: Habit? = null
    private var editingHabit = Habit()

    private val isInputCorrect: Boolean
        get() {
            val name = et_habit_name.text.toString()
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, R.string.toast_empty_name, Toast.LENGTH_SHORT).show()
                return false
            }

            val targetString = et_habit_target.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(targetString)) {
                Toast.makeText(this, R.string.toast_target_empty, Toast.LENGTH_LONG).show()
                return false
            }

            try {
                editingHabit.record.target = Integer.parseInt(targetString)
            } catch (e: NumberFormatException) {
                Toast.makeText(this, R.string.toast_failed_target_value, Toast.LENGTH_LONG).show()
                et_habit_target.requestFocus()
                return false
            }

            return true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configure()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_habit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
                onBackPressed()
                true
            }
            R.id.action_save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun configure() {
        setContentView(R.layout.activity_edit_habit)

        readExtras()

        val titleId = if (originalHabit == null)
            R.string.activity_create_label
        else
            R.string.activity_edit_label
        supportActionBar?.setTitle(titleId)

        val record = editingHabit.record
        et_habit_name.setText(record.name)
        if (record.color != HabitRecord.DEFAULT_COLOR) {
            et_habit_name.setTextColor(record.color)
        }

        et_habit_target.setText(record.target.toString())
        updateTimeText()

        val resetFrequencies = Arrays.asList(*ResetFrequency.ALL)
        val resetAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, resetFrequencies)
        spinner_reset.adapter = resetAdapter
        spinner_reset.prompt = resources.getString(R.string.spinner_prompt)

        val selection = if (originalHabit == null)
            ResetFrequency.NEVER
        else
            originalHabit!!.record.resetFreq
        spinner_reset.setSelection(resetFrequencies.indexOf(selection))

        spinner_reset.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selected = parent.getItemAtPosition(position).toString()
                editingHabit.record.resetFreq = selected
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        tv_reminder_time.setOnClickListener { onDateSpinnerClick() }
        bt_pick_color.setOnClickListener { showColorPicker() }
    }

    private fun readExtras() {
        if (intent.hasExtra(EDIT_HABIT_EXTRA_KEY)) {
            originalHabit = intent.getParcelableExtra(EDIT_HABIT_EXTRA_KEY)
            editingHabit = originalHabit!!.copy()
        }
    }

    private fun onDateSpinnerClick() {
        val timePickerFragment: TimePickerFragment
        timePickerFragment = if (editingHabit.isReminderOn) {
            val record = editingHabit.record
            TimePickerFragment.newInstance(record.reminderHour, record.reminderMin)
        } else {
            TimePickerFragment()
        }
        timePickerFragment.setOnTimeSetListener(this)
        timePickerFragment.show(supportFragmentManager, "TimePicker")
    }

    private fun showColorPicker() {
        val colorPicker = ColorPicker(this)
        colorPicker.setDefaultColorButton(editingHabit.record.color)
        colorPicker.positiveButton.setTextColor(resources.getColor(R.color.colorAccent))
        colorPicker.setRoundColorButton(true)
        colorPicker.setOnChooseColorListener(object : ColorPicker.OnChooseColorListener {
            override fun onChooseColor(position: Int, color: Int) {
                editingHabit.record.color = color
                et_habit_name.setTextColor(color)
            }

            override fun onCancel() {}
        })
        colorPicker.show()
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        editingHabit.record.reminderHour = hourOfDay
        editingHabit.record.reminderMin = minute
        updateTimeText()
    }

    override fun onCancel() {
        editingHabit.record.reminderHour = HabitRecord.REMINDER_OFF
        editingHabit.record.reminderMin = HabitRecord.REMINDER_OFF
        updateTimeText()
    }

    private fun updateTimeText() {
        if (editingHabit.isReminderOn) {
            val record = editingHabit.record
            tv_reminder_time.text = ReminderTime.getTimeString(record.reminderHour, record.reminderMin)
        } else {
            tv_reminder_time.setText(R.string.off)
        }
    }

    private fun save() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null || !isInputCorrect) return

        putChanges()

        if (originalHabit == null) {
            createNew()
        } else {
            applyChanges()
        }
    }

    private fun createNew() {
        HabitoAnalytics.logCreateHabitWithName(editingHabit.record.name)
        FirebaseSyncUtils.createNewHabitRecord(editingHabit.record)
        finish()
    }

    private fun applyChanges() {
        val data = Intent()
        data.putExtra(EDIT_HABIT_RESULT, editingHabit)
        setResult(Activity.RESULT_OK, data)

        ReminderUtils.processHabit(editingHabit, this)
        HabitoScoreUtils.resetScore(editingHabit)
        FirebaseSyncUtils.applyChangesForHabit(editingHabit)

        finish()
    }

    private fun putChanges() {
        val currentUser = FirebaseAuth.getInstance().currentUser!!
        val now = System.currentTimeMillis()
        val record = editingHabit.record

        if (originalHabit == null) {
            record.createdAt = now
            record.resetTimestamp = now
        }

        record.userId = currentUser.uid
        record.name = et_habit_name.text.toString().trim { it <= ' ' }
    }

    companion object {
        val EDIT_HABIT_RESULT = "com.ivanmagda.habito.activities.edit_result"
        val EDIT_HABIT_EXTRA_KEY = "com.ivanmagda.habito.activities.edit"
    }
}
