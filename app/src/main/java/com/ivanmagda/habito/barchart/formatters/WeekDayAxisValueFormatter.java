package com.ivanmagda.habito.barchart.formatters;

import com.github.mikephil.charting.components.AxisBase;
import com.ivanmagda.habito.utils.HabitoDateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeekDayAxisValueFormatter extends HabitoBaseIAxisValueFormatter {

    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE", Locale.getDefault());

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return FORMATTER.format(new Date(getDateForValue(value)));
    }

    @Override
    public long getDateForValue(float value) {
        HabitoDateUtils dateUtils = HabitoDateUtils.INSTANCE;

        Calendar calendar = dateUtils.getCalendarWithTime(dateUtils.getStartOfCurrentWeek());
        calendar.add(Calendar.DATE, (int) value);

        return calendar.getTimeInMillis();
    }

}

