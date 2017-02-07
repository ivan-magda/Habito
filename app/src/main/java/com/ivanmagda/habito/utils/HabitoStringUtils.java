package com.ivanmagda.habito.utils;

import android.support.annotation.NonNull;

public final class HabitoStringUtils {

    public static String capitalized(@NonNull final String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private HabitoStringUtils() {
    }

}
