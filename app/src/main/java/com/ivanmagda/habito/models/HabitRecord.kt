package com.ivanmagda.habito.models

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable

/**
 * When syncing with an Firebase Database DataSnapshot's should be parsed with this class.
 */
class HabitRecord(var userId: String? = null,
                  var createdAt: Long = System.currentTimeMillis(),
                  var name: String = "",
                  var color: Int = DEFAULT_COLOR,
                  var target: Int = 0,
                  var resetFreq: String = ResetFrequency.NEVER,
                  var resetTimestamp: Long = System.currentTimeMillis(),
                  var reminderHour: Int = REMINDER_OFF,
                  var reminderMin: Int = REMINDER_OFF,
                  score: Int = 0,
                  var checkmarks: MutableList<Long> = ArrayList()) : Parcelable {

    var score = score
        set(newValue) {
            if (newValue >= 0) {
                field = newValue
            }
        }

    constructor(parcel: Parcel) : this() {
        userId = parcel.readString()
        createdAt = parcel.readLong()
        name = parcel.readString()
        color = parcel.readInt()
        target = parcel.readInt()
        resetFreq = parcel.readString()
        resetTimestamp = parcel.readLong()
        reminderHour = parcel.readInt()
        reminderMin = parcel.readInt()
        score = parcel.readInt()
        parcel.readList(checkmarks, Long::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeLong(createdAt)
        parcel.writeString(name)
        parcel.writeInt(color)
        parcel.writeInt(target)
        parcel.writeString(resetFreq)
        parcel.writeLong(resetTimestamp)
        parcel.writeInt(reminderHour)
        parcel.writeInt(reminderMin)
        parcel.writeInt(score)
        parcel.writeList(checkmarks)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun copy(): HabitRecord {
        return HabitRecord(userId, createdAt, name, color, target, resetFreq, resetTimestamp,
                reminderHour, reminderMin, score, checkmarks)
    }

    override fun toString(): String {
        return "HabitRecord{" +
                "userId='" + userId + '\'' +
                ", createdAt=" + createdAt +
                ", name='" + name + '\'' +
                ", color=" + color +
                ", target=" + target +
                ", resetFreq='" + resetFreq + '\'' +
                ", resetTimestamp=" + resetTimestamp +
                ", reminderHour=" + reminderHour +
                ", reminderMin=" + reminderMin +
                ", score=" + score +
                ", checkmarks=" + checkmarks +
                '}'
    }

    companion object CREATOR : Parcelable.Creator<HabitRecord> {
        val REMINDER_OFF = -1
        val DEFAULT_COLOR = Color.WHITE

        override fun createFromParcel(parcel: Parcel): HabitRecord {
            return HabitRecord(parcel)
        }

        override fun newArray(size: Int): Array<HabitRecord?> {
            return arrayOfNulls(size)
        }
    }
}
