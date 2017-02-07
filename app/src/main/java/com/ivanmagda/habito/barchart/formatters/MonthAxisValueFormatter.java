package com.ivanmagda.habito.barchart.formatters;

import com.github.mikephil.charting.components.AxisBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.ivanmagda.habito.utils.HabitoDateUtils.getCalendarWithTime;
import static com.ivanmagda.habito.utils.HabitoDateUtils.getStartOfCurrentMonth;
import static com.ivanmagda.habito.utils.HabitoDateUtils.getStartOfWeek;

public final class MonthAxisValueFormatter extends HabitoBaseIAxisValueFormatter {

    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("M/d", Locale.getDefault());

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return FORMATTER.format(getDateForValue(value));
    }

    @Override
    public long getDateForValue(float value) {
        Calendar calendar = getCalendarWithTime(getStartOfCurrentMonth());
        calendar.set(Calendar.WEEK_OF_MONTH, (int) value + 1);
        long startOfWeek = getStartOfWeek(calendar.getTimeInMillis());

        if (new Date(startOfWeek).before(new Date(getStartOfCurrentMonth()))) {
            startOfWeek = getStartOfCurrentMonth();
        }

        return startOfWeek;
    }

}
