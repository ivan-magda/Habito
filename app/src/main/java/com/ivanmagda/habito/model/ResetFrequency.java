package com.ivanmagda.habito.model;

public class ResetFrequency {

    public enum Type {
        DAY(0), WEEK(1), MONTH(2), YEAR(3), NEVER(4);

        private final int value;

        private Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static final String DAY = "Day";
    public static final String WEEK = "Week";
    public static final String MONTH = "Month";
    public static final String YEAR = "Year";
    public static final String NEVER = "Never";

    public static final String[] ALL = new String[]{DAY, WEEK, MONTH, YEAR, NEVER};

    public static String stringFor(Type type) {
        switch (type) {
            case DAY:
                return DAY;
            case WEEK:
                return WEEK;
            case MONTH:
                return MONTH;
            case YEAR:
                return YEAR;
            case NEVER:
                return NEVER;
            default:
                throw new IllegalArgumentException("Unsupported frequency type");
        }
    }

    public static Type typeFrom(String stringType) {
        switch (stringType) {
            case DAY:
                return Type.DAY;
            case WEEK:
                return Type.WEEK;
            case MONTH:
                return Type.MONTH;
            case YEAR:
                return Type.YEAR;
            case NEVER:
                return Type.NEVER;
            default:
                throw new IllegalArgumentException("Illegal type name");
        }
    }

    private ResetFrequency() {
    }

}
