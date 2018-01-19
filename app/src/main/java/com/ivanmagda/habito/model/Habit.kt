package com.ivanmagda.habito.model

import android.os.Parcel
import android.os.Parcelable

import com.ivanmagda.habito.utils.HabitoScoreUtils

class Habit(var id: String? = null, var record: HabitRecord = HabitRecord()) : Parcelable, Cloneable {

    val isReminderOn: Boolean
        get() = record.reminderHour != HabitRecord.REMINDER_OFF && record.reminderMin != HabitRecord.REMINDER_OFF

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(HabitRecord::class.java.classLoader))

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeParcelable(record, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun copy(): Habit {
        return Habit(id, record.copy())
    }

    @Synchronized
    fun increaseScore() {
        HabitoScoreUtils.increaseScore(this)
    }

    @Synchronized
    fun decreaseScore() {
        HabitoScoreUtils.decreaseScore(this)
    }

    override fun toString(): String {
        return "Habit{" +
                "id='" + id + '\'' +
                ", record=" + record +
                '}'
    }

    companion object CREATOR : Parcelable.Creator<Habit> {
        override fun createFromParcel(parcel: Parcel): Habit {
            return Habit(parcel)
        }

        override fun newArray(size: Int): Array<Habit?> {
            return arrayOfNulls(size)
        }
    }
}
