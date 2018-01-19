package com.ivanmagda.habito.util

import com.ivanmagda.habito.model.ResetFrequency

class HabitoListUtils(private val dates: List<Long> = ArrayList()) {
    fun filteredBy(type: ResetFrequency.Type): List<Long> {
        if (type == ResetFrequency.Type.NEVER) {
            return dates
        }

        return dates.filter {
            HabitoDateUtils.isDateInType(it, type)
        }
    }
}
