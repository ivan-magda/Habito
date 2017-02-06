package com.ivanmagda.habito.models;

public class ResetFrequency {

    public enum Type {
        DAY(0), WEEK(1), MONTH(2), YEAR(3), NEVER(4);

        private final int value;

        Type(int value) {
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

    private Type mType;

    public ResetFrequency() {
        this.mType = Type.NEVER;
    }

    public ResetFrequency(Type type) {
        this.mType = type;
    }

    public ResetFrequency(String stringType) {
        this.mType = typeFrom(stringType);
    }

    public Type getType() {
        return mType;
    }

    public String getTypeName() {
        return ResetFrequency.stringFor(mType);
    }

    public void setType(Type type) {
        this.mType = type;
    }

    public void setType(String typeString) {
        this.mType = ResetFrequency.typeFrom(typeString);
    }

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

}
