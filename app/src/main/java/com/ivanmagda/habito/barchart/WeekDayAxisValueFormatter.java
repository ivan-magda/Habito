package com.ivanmagda.habito.barchart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.ivanmagda.habito.utils.HabitoDateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeekDayAxisValueFormatter implements IAxisValueFormatter {

    public static final int LABEL_COUNT = 7;
    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE", Locale.getDefault());

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(HabitoDateUtils.getStartOfThisWeek());
        calendar.add(Calendar.DATE, (int) value);
        return FORMATTER.format(calendar.getTime());
    }

}

