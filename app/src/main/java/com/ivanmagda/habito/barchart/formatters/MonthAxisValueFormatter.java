package com.ivanmagda.habito.barchart.formatters;

import com.github.mikephil.charting.components.AxisBase;
import com.ivanmagda.habito.utils.HabitoDateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class MonthAxisValueFormatter extends HabitoBaseIAxisValueFormatter {

    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("M/d", Locale.getDefault());

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return FORMATTER.format(getDateForValue(value));
    }

    @Override
    public long getDateForValue(float value) {
        HabitoDateUtils dateUtils = HabitoDateUtils.INSTANCE;

        Calendar calendar = dateUtils.getCalendarWithTime(dateUtils.getStartOfCurrentMonth());
        calendar.set(Calendar.WEEK_OF_MONTH, (int) value + 1);
        long startOfWeek = dateUtils.getStartOfWeek(calendar.getTimeInMillis());

        if (new Date(startOfWeek).before(new Date(dateUtils.getStartOfCurrentMonth()))) {
            startOfWeek = dateUtils.getStartOfCurrentMonth();
        }

        return startOfWeek;
    }

}
