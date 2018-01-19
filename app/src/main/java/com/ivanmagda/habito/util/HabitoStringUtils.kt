package com.ivanmagda.habito.util

class HabitoStringUtils {
    companion object {
        fun capitalized(string: String): String {
            return string.substring(0, 1).toUpperCase() + string.substring(1)
        }
    }
}
