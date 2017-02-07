package com.ivanmagda.habito.barchart;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.ivanmagda.habito.R;

import java.util.Arrays;
import java.util.List;

public final class HabitoBarChartRange {

    public enum DateRange {
        WEEK, MONTH, YEAR;

        public String stringValue(@NonNull final Context context) {
            Resources resources = context.getResources();
            switch (this) {
                case WEEK:
                    return resources.getString(R.string.week);
                case MONTH:
                    return resources.getString(R.string.month);
                case YEAR:
                    return resources.getString(R.string.year);
                default:
                    throw new IllegalArgumentException("Receive illegal date range string");
            }
        }

        public static DateRange fromString(@NonNull String string, @NonNull final Context context) {
            if (string.equals(WEEK.stringValue(context))) {
                return WEEK;
            } else if (string.equals(MONTH.stringValue(context))) {
                return MONTH;
            } else if (string.equals(YEAR.stringValue(context))) {
                return YEAR;
            } else {
                throw new IllegalArgumentException("Illegal date string");
            }
        }
    }

    public static List<String> allStringValues(@NonNull final Context context) {
        return Arrays.asList(DateRange.WEEK.stringValue(context),
                DateRange.MONTH.stringValue(context),
                DateRange.YEAR.stringValue(context));
    }

    private HabitoBarChartRange() {
    }

}
