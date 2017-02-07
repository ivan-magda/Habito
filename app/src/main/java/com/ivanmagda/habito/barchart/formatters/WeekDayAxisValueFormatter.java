package com.ivanmagda.habito.barchart.formatters;

import com.github.mikephil.charting.components.AxisBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.ivanmagda.habito.utils.HabitoDateUtils.getCalendarWithTime;
import static com.ivanmagda.habito.utils.HabitoDateUtils.getStartOfCurrentWeek;

public class WeekDayAxisValueFormatter extends HabitoBaseIAxisValueFormatter {

    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE", Locale.getDefault());

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return FORMATTER.format(new Date(getDateForValue(value)));
    }

    @Override
    public long getDateForValue(float value) {
        Calendar calendar = getCalendarWithTime(getStartOfCurrentWeek());
        calendar.add(Calendar.DATE, (int) value);
        return calendar.getTimeInMillis();
    }

}

