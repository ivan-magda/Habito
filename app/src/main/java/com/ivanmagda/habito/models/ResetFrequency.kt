package com.ivanmagda.habito.models

class ResetFrequency(type: Type = Type.NEVER) {

    var type = type
        private set(value) {
            field = value
        }

    val typeName: String
        get() = ResetFrequency.stringFor(type)

    enum class Type(val value: Int) {
        DAY(0), WEEK(1), MONTH(2), YEAR(3), NEVER(4)
    }

    constructor(stringType: String) : this() {
        type = typeFrom(stringType)
    }

    fun setType(typeString: String) {
        type = typeFrom(typeString)
    }

    companion object {

        val DAY = "Day"
        val WEEK = "Week"
        val MONTH = "Month"
        val YEAR = "Year"
        val NEVER = "Never"

        val ALL = arrayOf(DAY, WEEK, MONTH, YEAR, NEVER)

        fun stringFor(type: Type?): String {
            return when (type) {
                ResetFrequency.Type.DAY -> DAY
                ResetFrequency.Type.WEEK -> WEEK
                ResetFrequency.Type.MONTH -> MONTH
                ResetFrequency.Type.YEAR -> YEAR
                ResetFrequency.Type.NEVER -> NEVER
                else -> throw IllegalArgumentException("Unsupported frequency type")
            }
        }

        fun typeFrom(stringType: String): Type {
            return when (stringType) {
                DAY -> Type.DAY
                WEEK -> Type.WEEK
                MONTH -> Type.MONTH
                YEAR -> Type.YEAR
                NEVER -> Type.NEVER
                else -> throw IllegalArgumentException("Illegal type name")
            }
        }
    }
}
