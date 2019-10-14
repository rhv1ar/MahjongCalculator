package com.yoshino.mahjong.calculator.models;


public class FanModel {
  private final int mNum;
  private final String mName;
  private final boolean mIsYakuman;

  public int getNum() {
    return mNum;
  }

  public String getNumString() {
    return mNum + "翻";
  }
  public String getName() {
    return mName;
  }
  public boolean isYakuman() {
    return mIsYakuman;
  }

  public FanModel(final int num, final String name) {
    mNum = num;
    mName = name;
    mIsYakuman = false;
  }

  public FanModel(final String name) {
    mNum = 0;
    mName = name;
    mIsYakuman = true;
  }
}
