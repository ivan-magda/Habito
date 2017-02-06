package com.ivanmagda.habito.models;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * When syncing with an Firebase Database DataSnapshot's should be parsed with this class.
 */
public final class HabitRecord implements Parcelable {

    public static final int REMINDER_OFF = -1;
    public static final int DEFAULT_COLOR = Color.WHITE;

    private String userId;
    private long createdAt;
    private String name;
    private int color;
    private int target;
    private String resetFreq;
    private long resetTimestamp;
    private int reminderHour;
    private int reminderMin;
    private int score;
    private List<Long> checkmarks;

    public HabitRecord() {
        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.name = "";
        this.color = DEFAULT_COLOR;
        this.target = 0;
        this.resetFreq = ResetFrequency.NEVER;
        this.resetTimestamp = now;
        this.reminderHour = REMINDER_OFF;
        this.reminderMin = REMINDER_OFF;
        this.score = 0;
        this.checkmarks = new ArrayList<>();
    }

    public HabitRecord(String userId, long createdAt, String name, int color, int target,
                       String resetFreq, long resetTimestamp, int reminderHour, int reminderMin,
                       int score, List<Long> checkmarks) {
        this.userId = userId;
        this.createdAt = createdAt;
        this.name = name;
        this.color = color;
        this.target = target;
        this.resetFreq = resetFreq;
        this.resetTimestamp = resetTimestamp;
        this.reminderHour = reminderHour;
        this.reminderMin = reminderMin;
        this.score = score;
        this.checkmarks = checkmarks;
    }

    public HabitRecord(Parcel in) {
        this.userId = in.readString();
        this.createdAt = in.readLong();
        this.name = in.readString();
        this.color = in.readInt();
        this.target = in.readInt();
        this.resetFreq = in.readString();
        this.resetTimestamp = in.readLong();
        this.reminderHour = in.readInt();
        this.reminderMin = in.readInt();
        this.score = in.readInt();
        this.checkmarks = new ArrayList<>();
        in.readList(this.checkmarks, null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(userId);
        dest.writeLong(createdAt);
        dest.writeString(name);
        dest.writeInt(color);
        dest.writeInt(target);
        dest.writeString(resetFreq);
        dest.writeLong(resetTimestamp);
        dest.writeInt(reminderHour);
        dest.writeInt(reminderMin);
        dest.writeInt(score);
        dest.writeList(checkmarks);
    }

    public static final Parcelable.Creator<HabitRecord> CREATOR = new Parcelable.Creator<HabitRecord>() {
        @Override
        public HabitRecord createFromParcel(Parcel parcel) {
            return new HabitRecord(parcel);
        }

        @Override
        public HabitRecord[] newArray(int size) {
            return new HabitRecord[size];
        }
    };

    public HabitRecord copy() {
        return new HabitRecord(userId, createdAt, name, color, target, resetFreq, resetTimestamp,
                reminderHour, reminderMin, score, checkmarks);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getResetFreq() {
        return resetFreq;
    }

    public void setResetFreq(String resetFreq) {
        this.resetFreq = resetFreq;
    }

    public long getResetTimestamp() {
        return resetTimestamp;
    }

    public void setResetTimestamp(long resetTimestamp) {
        this.resetTimestamp = resetTimestamp;
    }

    public int getReminderHour() {
        return reminderHour;
    }

    public void setReminderHour(int reminderHour) {
        this.reminderHour = reminderHour;
    }

    public int getReminderMin() {
        return reminderMin;
    }

    public void setReminderMin(int reminderMin) {
        this.reminderMin = reminderMin;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if (score < 0) return;
        this.score = score;
    }

    public List<Long> getCheckmarks() {
        return checkmarks;
    }

    public void setCheckmarks(List<Long> checkmarks) {
        this.checkmarks = checkmarks;
    }

    @Override
    public String toString() {
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
                '}';
    }

}
