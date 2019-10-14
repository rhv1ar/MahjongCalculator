package com.yoshino.mahjong.calculator.models;


import com.yoshino.mahjong.calculator.MyException;
import com.yoshino.mahjong.calculator.statics.Wind;
import com.yoshino.mahjong.calculator.utils.Tile;
import com.yoshino.mahjong.calculator.utils.Tiles;
import com.yoshino.mahjong.calculator.utils.Yama;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static com.yoshino.mahjong.calculator.MyException.*;


public class BoardModel {
    /**
     * Class tag.
     */
    private static final String TAG = BoardModel.class.getSimpleName();

    private static BoardModel mBoardModel = new BoardModel();

    public static BoardModel getInstance() {
        return mBoardModel;
    }

    // Round wind
    private final BehaviorSubject<Wind> wind = BehaviorSubject.createDefault(Wind.EAST);
    // 積み棒
    private final BehaviorSubject<Integer> stick100 = BehaviorSubject.createDefault(0);


    public final Observable<String> formattedWind;
    public final Observable<String> formattedStick100;

    public int getStick100() {
        return stick100.getValue();
    }
    public void increaseStick100() {
        stick100.onNext(stick100.getValue() + 1);
    }

    //リー棒供託
    //private int mStick1000;

    private Tiles mYama;

    private BoardModel() {

        initYama();
        formattedWind = wind.map(Wind::toString);
        formattedStick100 = stick100.map(Objects::toString);
    }

    private void initYama() {
        mYama = new Yama("yama", 136);
        ((Yama) mYama).init();
    }

    Tile getTile(final Tile target) throws TileShortageException {
        return ((Yama) mYama).getTile(target);
    }

    void addTile(final Tile tile) {
        mYama.addTile(tile);
    }

    public void nextWind() {
        wind.onNext(wind.getValue().next());
    }

    public void initialize() {
        initYama();
        wind.onNext(Wind.EAST);
    }

    public Wind getWind() {
        return wind.getValue();
    }
}
