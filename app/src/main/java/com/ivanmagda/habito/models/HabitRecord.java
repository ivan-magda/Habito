package com.ivanmagda.habito.models;

import java.util.List;

public final class HabitRecord {

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
        this.score = score;
    }

    public List<Long> getCheckmarks() {
        return checkmarks;
    }

    public void setCheckmarks(List<Long> checkmarks) {
        this.checkmarks = checkmarks;
    }

}
