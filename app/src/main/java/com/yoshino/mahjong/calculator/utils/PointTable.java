package com.yoshino.mahjong.calculator.utils;

public class PointTable {
  private static final String TAG = PointTable.class.getSimpleName();
  private final FanCalculator mFanCalculator;
  private final FuCalculator mFuCalculator;
  private final int mStick100;
  private final boolean mIsParent;

  public boolean isParent() {
    return mIsParent;
  }

  private int mTotalPoint;
  private String mName;

  public int getTotalPoint() {
    return mTotalPoint;
  }

  public PointTable(final FanCalculator fanCalculator,
                    final FuCalculator fuCalculator,
                    final int stick100,
                    final boolean isParent) {
    mFanCalculator = fanCalculator;
    mFuCalculator = fuCalculator;
    mStick100 = stick100;
    mIsParent = isParent;
  }

  void calculate() {
    final double coefficient = (mIsParent) ? 1.5 : 1;
    int fan = mFanCalculator.getSumFan();
    int fu = mFuCalculator.getSumFu();

    // 役満
    if (mFanCalculator.hasYakuman()) {
      int yakuman = mFanCalculator.getSumYakuman();
      switch (yakuman) {
        case 1:
          mName = "役満";
          break;
        case 2:
          mName = "ダブル役満";
          break;
        case 3:
          mName = "トリプル役満";
          break;
        case 4:
          mName = "四倍役満";
          break;
        default:
          mName = "役満";
          break;
      }
      mTotalPoint = (int)(32000 * coefficient) * yakuman;
      return;
    }

    // buta
    if (fan == 0) {
      mName = "役無し";
      mTotalPoint = 0;
      return;
    }

    // 満貫
    if (fan == 3 && fu >= 70
            || fan == 4 && fu >= 40
            || fan == 5) {
      mName = "満貫";
      mTotalPoint = (int)(8000 * coefficient);
      return;
    }

    // 跳満
    if (fan >= 6 && fan <= 7) {
      mName = "跳満";
      mTotalPoint = (int)(12000 * coefficient);
      return;
    }

    // 倍満
    if (fan >= 8 && fan <= 10) {
      mName = "倍満";
      mTotalPoint = (int)(16000 * coefficient);
      return;
    }

    // 三倍満
    if (fan >= 11 && fan <= 12) {
      mName = "三倍満";
      mTotalPoint = (int)(24000 * coefficient);
      return;
    }

    // 数え役満
    if (fan >= 13) {
      mName = "数え役満";
      mTotalPoint = (int)(32000 * coefficient);
      return;
    }

    // その他
    mName = "";
    mTotalPoint = roundedUp10((int)(fu * 4 * coefficient * Math.pow(2, fan + 2)));
  }

  int getRonPoint() {
    return mTotalPoint + mStick100 * 300;
  }

  public int getTsumoPointFromParent() {
    return roundedUp10(mTotalPoint / 2) + mStick100 * 100;
  }

  public int getTsumoPointFromChild() {
    if (mIsParent) {
      return roundedUp10(mTotalPoint / 3) + mStick100 * 100;
    } else {
      return roundedUp10(mTotalPoint / 4) + mStick100 * 100;
    }
  }

  private int roundedUp10(final int x) {
    if (x % 100 == 0) {
      return x;
    }
    return (x / 100 + 1) * 100;
  }
}
