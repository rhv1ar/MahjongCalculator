package com.yoshino.mahjong.calculator.utils;

import android.util.Log;

import com.yoshino.mahjong.calculator.models.BoardModel;
import com.yoshino.mahjong.calculator.models.PlayerModel;
import com.yoshino.mahjong.calculator.statics.Wind;

import java.util.ArrayList;

public class FuCalculator extends ArrayList<FuCalculator.Fu> {
  private final static String TAG = FuCalculator.class.getSimpleName();

  private Combination mCombination;
  private boolean mHasPeace;
  private Wind mSeatWind;
  private Wind mRoundWind;

  public class Fu {
    private int mNum;
    private String mName;
    public int getNum() {
      return mNum;
    }
    public String getName() {
      return mName;
    }
    public Fu(final int num, final String name) {
      mNum = num;
      mName = name;
    }
    private boolean isChitoitsuFu() {
      return mName.equals("七対子符");
    }
  }

  public FuCalculator(final Combination combination,
                      final PlayerModel playerModel,
                      final BoardModel boardModel,
                      final boolean hasPreace) {
    mCombination = combination;
    mHasPeace = hasPreace;
    mSeatWind = playerModel.getWind();
    mRoundWind = boardModel.getWind();
  }

  public int getSumFu() {
    int count = 0;
    for (Fu fu : this) {
      count += fu.getNum();
      if (fu.isChitoitsuFu()) {
        return count;
      }
    }
    return roundedUp(count);
  }



  void calculate(final boolean isTsumo) {
    Tile horaTile = mCombination.getHoraTile();

    switch (mCombination.getType()) {
      case NORMAL:
        break;
      case CHITOITSU:
        add(new Fu(25, "七対子符"));
        Log.v(TAG, "hit");
        return;
      case KOKUSHIMUSOU:
        return;
    }


    // 平和ツモ
    if (mHasPeace) {
      add(new Fu(20, "副底"));
      return;
    }

    // ツモ符
    if (isTsumo) {
      add(new Fu(2, "自摸符"));
    }

    // 門前ロン符
    if (!mCombination.isFuro() && !isTsumo) {
      add(new Fu(10, "門前加符"));
    }

    // 雀頭符
    Tile head = mCombination.getHead();
    if (mRoundWind.toString().equals(head.getName())        // 場風符
            || mSeatWind.toString().equals(head.getName())  // 自風符
            || head.isDragon()) {                           // 三元牌符
      add(new Fu(2, "雀頭符"));
    }

    // 待ち符
    boolean isShabo = false, isRyanmen = false;
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getType()) {
        case SHUNTSU:
          if (mentsu.getFirstTile().isCorner()) {
            if (mentsu.getFirstTile().equalTile(horaTile)) {
              isRyanmen = true;
            }
          } else if (mentsu.getLastTile().isCorner()) {
            if (mentsu.getLastTile().equalTile(horaTile)) {
              isRyanmen = true;
            }
          } else {
            if (mentsu.getFirstTile().equalTile(horaTile) || mentsu.getLastTile().equalTile(horaTile)) {
              isRyanmen = true;
            }
          }
          break;
        case KOHTSU:
          if (mentsu.getFirstTile().equalTile(horaTile)) {
            isShabo = true;
          }
          break;
        default:
          break;
      }
    }

    if (!isShabo && !isRyanmen) {
      add(new Fu(2, "待ち符"));
    }

    // 面子符
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      Tile tile = mentsu.getFirstTile();
      switch (mentsu.getType()) {
        case KOHTSU:
          if (isTsumo) {
            if (tile.isCorner()) {
              add(new Fu(8, "暗刻符(ヤオ九牌)"));
            } else {
              add(new Fu(4, "暗刻符(中張牌)"));
            }
          } else {
            if (isShabo) {
              if (tile.getSerialNum() == horaTile.getSerialNum()) {
                if (tile.isCorner()) {
                  add(new Fu(4, "明刻符(ヤオ九牌)"));
                } else {
                  add(new Fu(2, "明刻符(中張牌)"));
                }
              } else {
                if (tile.isCorner()) {
                  add(new Fu(8, "暗刻符(ヤオ九牌)"));
                } else {
                  add(new Fu(4, "暗刻符(中張牌)"));
                }
              }
            } else {
              if (tile.isCorner()) {
                add(new Fu(8, "暗刻符(ヤオ九牌)"));
              } else {
                add(new Fu(4, "暗刻符(中張牌)"));
              }
            }
          }
          break;
        case PON:
          if (tile.isCorner()) {
            add(new Fu(4, "明刻符(ヤオ九牌)"));
          } else {
            add(new Fu(2, "明刻符(中張牌)"));
          }
          break;
        case KAN_LIGHT:
          if (tile.isCorner()) {
            add(new Fu(16, "明槓符(ヤオ九牌)"));
          } else {
            add(new Fu(8, "明槓符(中張牌)"));
          }
          break;
        case KAN_DARK:
          if (tile.isCorner()) {
            add(new Fu(32, "暗槓符(ヤオ九牌)"));
          } else {
            add(new Fu(16, "暗槓符(中張牌)"));
          }
          break;
        default:
          break;
      }
    }


    if (size() == 0) {
      add(new Fu(30, "鳴き平和"));
    } else {
      add(0, new Fu(20, "副底"));
    }
  }

  private int roundedUp(final int x) {
    if (x % 10 == 0) {
      return x;
    }
    return (x / 10 + 1) * 10;
  }

}
