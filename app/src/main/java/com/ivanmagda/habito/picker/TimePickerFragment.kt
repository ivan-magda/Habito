package com.ivanmagda.habito.picker

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.text.format.DateFormat
import android.widget.TimePicker
import com.ivanmagda.habito.R
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var mOnTimeSetListener: OnTimeSetListener? = null
    private var mHour: Int = 0
    private var mMinutes: Int = 0

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    interface OnTimeSetListener {

        /**
         * @param view      The time picker view.
         * @param hourOfDay The hour that was set.
         * @param minute    The minute that was set.
         */
        fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int)

        fun onCancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        if (args != null) {
            this.mHour = args.getInt(HOUR_EXTRA_KEY)
            this.mMinutes = args.getInt(MINUTES_EXTRA_KEY)
        } else {
            // Use the current time as the default values for the time picker.
            val calendar = Calendar.getInstance()
            mHour = calendar.get(Calendar.HOUR_OF_DAY)
            mMinutes = calendar.get(Calendar.MINUTE)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = TimePickerDialog(activity, this, mHour, mMinutes, DateFormat.is24HourFormat(activity))
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.off)) { _, _ ->
            mOnTimeSetListener?.onCancel()
        }

        return dialog
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        mOnTimeSetListener?.onTimeSet(view, hourOfDay, minute)
    }

    fun setOnTimeSetListener(onTimeSetListener: OnTimeSetListener) {
        this.mOnTimeSetListener = onTimeSetListener
    }

    companion object {

        val HOUR_EXTRA_KEY = "hour"
        val MINUTES_EXTRA_KEY = "minutes"

        fun newInstance(hour: Int, minutes: Int): TimePickerFragment {
            val timePickerFragment = TimePickerFragment()

            val args = Bundle()
            args.putInt(HOUR_EXTRA_KEY, hour)
            args.putInt(MINUTES_EXTRA_KEY, minutes)
            timePickerFragment.arguments = args

            return timePickerFragment
        }
    }
}
