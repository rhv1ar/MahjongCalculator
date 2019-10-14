package com.yoshino.mahjong.calculator.utils;

import com.yoshino.mahjong.calculator.utils.Call;
import com.yoshino.mahjong.calculator.utils.Tile;

import java.util.ArrayList;

public class Mentsu extends ArrayList<Tile> {

    public enum Type {
        HEAD, HAND, TOITSU, KOHTSU, SHUNTSU, CHEW, PON, KAN_LIGHT, KAN_DARK, EMPTY, UNKNOWN;
    }

    /**
     * This value has mentsu type.
     */
    private Type mType;

    public Mentsu(final Call call) {
        mType = call.getType();
        addAll(call);
    }


    public Mentsu(final int num, final Type type) {
        mType = type;
        switch (type) {
            case HAND:
                add(new Tile(num));
                break;
            case TOITSU:
            case HEAD:
                add(new Tile(num));
                add(new Tile(num));
                break;
            case KOHTSU:
                add(new Tile(num));
                add(new Tile(num));
                add(new Tile(num));
                break;
            case SHUNTSU:
                add(new Tile(num));
                add(new Tile(num + 1));
                add(new Tile(num + 2));
                break;
            default:
                break;
        }
    }

    public Type getType() {
        return mType;
    }

    public Tile getFirstTile() {
        Tile min = get(0);
        for (int i = 1; i < this.size(); i++) {
            Tile tile = get(i);
            if (min.getSerialNum() > tile.getSerialNum()) {
                min =  tile;
            }
        }
        return min;
    }

    Tile getLastTile() {
        Tile max = get(0);
        for (int i = 1; i < this.size(); i++) {
            Tile tile = get(i);
            if (max.getSerialNum() < tile.getSerialNum()) {
                max =  tile;
            }
        }
        return max;
    }
}