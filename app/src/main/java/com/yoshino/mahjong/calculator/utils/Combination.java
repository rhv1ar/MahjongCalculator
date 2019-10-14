package com.yoshino.mahjong.calculator.utils;

import android.util.Log;

import com.yoshino.mahjong.calculator.models.BoardModel;
import com.yoshino.mahjong.calculator.models.PlayerModel;
import com.yoshino.mahjong.calculator.models.FanModel;

import java.util.ArrayList;


public class Combination {
  /**
   * Class tag.
   */
  private static final String TAG = Combination.class.getSimpleName();

  private ArrayList<Mentsu> mMentsuList = new ArrayList<>();
  private FanCalculator mFanCalculator;
  private FuCalculator mFuCalculator;
  private PointTable mPointTable;

  public ArrayList<Mentsu> getMentsuList() {
    return mMentsuList;
  }
  public void addMentsu(final Mentsu mentsu) {
    mMentsuList.add(mentsu);
  }
  public int mentsuSize() {
    return mMentsuList.size();
  }
  public FanCalculator getFanCalculator() {
    return mFanCalculator;
  }
  public FuCalculator getFuCalculator() {
    return mFuCalculator;
  }
  public PointTable getPointTable() {
    return mPointTable;
  }


  /**
   * Hora type.
   */
  public enum Type {
    NORMAL, CHITOITSU, KOKUSHIMUSOU;
  }

  private Type mType;
  public Type getType() {
    return mType;
  }

  // 和了牌
  private Tile mHoraTile;
  Tile getHoraTile() {
    return mHoraTile;
  }

  public boolean isParent() {
    return mPointTable.isParent();
  }
  private boolean mIsTsumo;

  ArrayList<Tile> getTileList() {
    ArrayList<Tile> list = new ArrayList<>();
    for (Mentsu mentsu : mMentsuList) {
      list.addAll(mentsu);
    }
    return list;
  }

  public Combination(final Type type) {
    mType = type;
  }

  private Combination(final ArrayList<Mentsu> mentsuList,
                      final FanCalculator fan,
                      final FuCalculator fu,
                      final PointTable point,
                      final boolean isTsumo) {
    mMentsuList = mentsuList;
    mFanCalculator = fan;
    mFuCalculator = fu;
    mPointTable = point;
    mIsTsumo = isTsumo;
  }

  /**
   * Constructor for clone.
   * @param mentsuList is cloned mentsu member
   */
  private Combination(final ArrayList<Mentsu> mentsuList,
                      final Type type,
                      final Tile horaTile) {
    mMentsuList = mentsuList;
    mType = type;
    mHoraTile = horaTile;
  }

  public void addCalls(final ArrayList<Call> callList) {
    for (Call call : callList) {
      mMentsuList.add(new Mentsu(call));
    }
  }

  public void setHoraTile(final Tile horaTile) {
    mHoraTile = horaTile;
  }

  public void print() {
    StringBuilder sb = new StringBuilder();

    // 点数
    if (mIsTsumo) {
      sb.append("ツモ|");
      if (isParent()) {
        sb.append(mPointTable.getTsumoPointFromChild());
        sb.append(" ALL");
      } else {
        sb.append(mPointTable.getTsumoPointFromChild());
        sb.append("/");
        sb.append(mPointTable.getTsumoPointFromParent());

      }
    } else {
      sb.append("ロン|");
      sb.append(mPointTable.getRonPoint());
    }
    sb.append("\n");

    // 面子
    for (Mentsu mentsu : mMentsuList) {
      for (Tile tile : mentsu) {
        sb.append(tile.getName());
      }
      sb.append("|");
    }
    sb.append("\n");

    // 翻
    sb.append(mFanCalculator.getSumFan());
    sb.append("翻");
    for (FanModel fan : mFanCalculator) {
      sb.append("|");
      sb.append(fan.getName());
      sb.append("(");
      sb.append(fan.getNum());
      sb.append(")");
    }
    sb.append("\n");

    // 符
    sb.append(mFuCalculator.getSumFu());
    sb.append("符");
    for (FuCalculator.Fu fu : mFuCalculator) {
      sb.append("|");
      sb.append(fu.getName());
      sb.append("(");
      sb.append(fu.getNum());
      sb.append(")");
    }
    sb.append("\n");


    Log.v(TAG, sb.toString());

  }

  public Combination calculate(final PlayerModel playerModel,
                               final BoardModel boardModel,
                               final boolean isTsumo) {
    FanCalculator fan = new FanCalculator(this, playerModel, boardModel);
    fan.calculate(isTsumo);
    FuCalculator fu = new FuCalculator(this, playerModel, boardModel, fan.hasPeace());
    if (!fan.hasYakuman()) {
      fu.calculate(isTsumo);
    }

    int stick100 = boardModel.getStick100();
    boolean isParent = playerModel.isParent();
    PointTable pointTable = new PointTable(fan, fu, stick100, isParent);
    pointTable.calculate();
    return new Combination(mMentsuList, fan, fu, pointTable, isTsumo);
  }


  boolean isFuro() {
    for (Mentsu mentsu : mMentsuList) {
      switch (mentsu.getType()) {
        case PON:
        case CHEW:
        case KAN_LIGHT:
          return true;
        default:
          break;
      }
    }
    return false;
  }

  Tile getHead() {
    for (Mentsu mentsu : mMentsuList) {
      if (mentsu.getType() == Mentsu.Type.HEAD) {
        return mentsu.getFirstTile();
      }
    }
    return null;
  }

  public boolean compareCombination(final Combination comb) {
    int maxPoint = mPointTable.getTotalPoint();
    int currentPoint = comb.mPointTable.getTotalPoint();
    if (currentPoint > maxPoint) {
      return true;
    } else if (currentPoint == maxPoint) {
      int maxFan = mFanCalculator.getSumFan();
      int currentFan = comb.mFanCalculator.getSumFan();
      int maxFu = mFuCalculator.getSumFu();
      int currentFu = comb.mFuCalculator.getSumFu();
      if (currentFan >= maxFan && currentFu >= maxFu) {
        return true;
      }
    }
    return false;
  }

  public Combination clone() {
    ArrayList<Mentsu> mentsuList = (ArrayList<Mentsu>) mMentsuList.clone();
    return new Combination(mentsuList, mType, mHoraTile);
  }
}
