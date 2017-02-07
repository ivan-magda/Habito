package com.ivanmagda.habito.barchart.formatters;

import com.github.mikephil.charting.components.AxisBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.ivanmagda.habito.utils.HabitoDateUtils.getCurrentCalendar;

public final class YearAxisValueFormatter extends HabitoBaseIAxisValueFormatter {

    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("MMM", Locale.getDefault());

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String yearString = FORMATTER.format(new Date(getDateForValue(value)));
        return yearString.substring(0, 1);
    }

    @Override
    public long getDateForValue(float value) {
        Calendar calendar = getCurrentCalendar();
        calendar.set(Calendar.MONTH, (int) value);
        return calendar.getTimeInMillis();
    }

}
