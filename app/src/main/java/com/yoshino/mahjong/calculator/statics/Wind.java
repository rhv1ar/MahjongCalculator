package com.yoshino.mahjong.calculator.statics;

public enum Wind {
    EAST("東"), SOUTH("南"), WEST("西"), NORTH("北");
    private String mName;
    public String toString() {
        return mName;
    }
    public boolean isParent() {
        return this.equals(EAST);
    }
    public Wind next() {
        boolean flag = false;
        for (Wind wind : Wind.values()) {
            if (flag) {
                return wind;
            }
            flag = wind.equals(this);
        }
        return Wind.EAST;
    }
    Wind(final String name) {
        mName = name;
    }
}
