package com.ivanmagda.habito.utils;

import android.support.annotation.NonNull;

import com.ivanmagda.habito.models.ResetFrequency;

import java.util.ArrayList;
import java.util.List;

public final class HabitoListUtils {

    private List<Long> mDates = new ArrayList<>();

    public HabitoListUtils(@NonNull List<Long> dates) {
        this.mDates = new ArrayList<>(dates);
    }

    public List<Long> filteredBy(ResetFrequency.Type type) {
        if (type == ResetFrequency.Type.NEVER) return mDates;

        List<Long> resultList = new ArrayList<>(mDates.size());
        for (Long date : mDates) {
            if (HabitoDateUtils.isDateInType(date, type)) resultList.add(date);
        }

        return resultList;
    }

}
