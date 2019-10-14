package com.yoshino.mahjong.calculator.utils;


import androidx.annotation.NonNull;

import com.yoshino.mahjong.calculator.R;

import java.io.Serializable;

public class Tile implements Serializable {
  enum Type {
    MANZU("萬"), SOUZU("索"), PINZU("筒"), WIND("風"), DRAGON("三元");
    private String mName;
    String getName() {
      return mName;
    }
    Type(final String name) {
      mName = name;
    }
  }
  private static final String[] WINDS = {"東", "南", "西", "北"};
  private static final String[] DRAGONS = {"白", "發", "中"};

  private Type mType;
  private int mSerialNum;
  private String mName;

  // variable fo view
  private int mImageId;
  private boolean mIsSelected = false;

  private boolean mIsCorner;
  private boolean mIsGreen;
  private boolean mIsWheel;
  private boolean mIsHonor;

  public int getSerialNum() {
    return mSerialNum;
  }
  public int getNum() {
    return mSerialNum % 10;
  }
  public String getName() {
    return mName;
  }
  boolean isCorner() {
    return mIsCorner;
  }
  boolean isGreen() {
    return mIsGreen;
  }
  boolean isWheel() {
    return mIsWheel;
  }
  boolean isHonor() {
    return mIsHonor;
  }

  public Tile(final int serialNum) {
    mSerialNum = serialNum;
    int num = mSerialNum % 10;
    int typeNum = mSerialNum / 10;

    switch (typeNum) {
      case 0:
        mType = Type.MANZU;
        mName = num + mType.getName();
        mIsCorner = num % 10 == 1 || num % 10 == 9;
        break;
      case 1:
        mType = Type.SOUZU;
        mName = num + mType.getName();
        mIsCorner = num % 10 == 1 || num % 10 == 9;
        if (num == 2 || num == 3 || num == 4 || num == 6 || num == 8) {
          mIsGreen = true;
        }
        break;
      case 2:
        mType = Type.PINZU;
        mName = num + mType.getName();
        mIsCorner = num % 10 == 1 || num % 10 == 9;
        if (num >= 2 && num <= 8) {
          mIsWheel = true;
        }
        break;
      case 3:
        mIsCorner = true;
        mIsHonor = true;
        if (mSerialNum >= 31 && mSerialNum <= 34) {
          mType = Type.WIND;
          mName = WINDS[num - 1];
        } else {
          mType = Type.DRAGON;
          mName = DRAGONS[num - 5];
          if (num == 6) {
            mIsGreen = true;
          }
        }
        break;
      default:
        break;
    }

    mImageId = changeImageId(mSerialNum);
  }

  private int changeImageId(final int serialNum) {
    switch (serialNum) {
      case 0:
        return R.mipmap.back;
      case 1:
        return R.mipmap.m1;
      case 2:
        return R.mipmap.m2;
      case 3:
        return R.mipmap.m3;
      case 4:
        return R.mipmap.m4;
      case 5:
        return R.mipmap.m5;
      case 6:
        return R.mipmap.m6;
      case 7:
        return R.mipmap.m7;
      case 8:
        return R.mipmap.m8;
      case 9:
        return R.mipmap.m9;
      case 11:
        return R.mipmap.s1;
      case 12:
        return R.mipmap.s2;
      case 13:
        return R.mipmap.s3;
      case 14:
        return R.mipmap.s4;
      case 15:
        return R.mipmap.s5;
      case 16:
        return R.mipmap.s6;
      case 17:
        return R.mipmap.s7;
      case 18:
        return R.mipmap.s8;
      case 19:
        return R.mipmap.s9;
      case 21:
        return R.mipmap.p1;
      case 22:
        return R.mipmap.p2;
      case 23:
        return R.mipmap.p3;
      case 24:
        return R.mipmap.p4;
      case 25:
        return R.mipmap.p5;
      case 26:
        return R.mipmap.p6;
      case 27:
        return R.mipmap.p7;
      case 28:
        return R.mipmap.p8;
      case 29:
        return R.mipmap.p9;
      case 31:
        return R.mipmap.j1;
      case 32:
        return R.mipmap.j2;
      case 33:
        return R.mipmap.j3;
      case 34:
        return R.mipmap.j4;
      case 35:
        return R.mipmap.j5;
      case 36:
        return R.mipmap.j6;
      case 37:
        return R.mipmap.j7;
      default:
        return R.mipmap.back;
    }
  }

  Type getType() {
    return mType;
  }

  boolean isWind() {
    return mType.equals(Type.WIND);
  }

  boolean isDragon() {
    return mType.equals(Type.DRAGON);
  }

  boolean equalTile(final Tile tile) {
    return mSerialNum == tile.getSerialNum();
  }

  public int getImageId() {
    return mImageId;
  }

  int getHeightPosition() {
    return mIsSelected ? 20 : 0;
  }

  void setSelected(final boolean isSelected) {
    mIsSelected = isSelected;
  }

  public boolean isSelected() {
    return mIsSelected;
  }

  public void defaultViewParameters() {
    openImage();
    setSelected(false);
  }

  boolean isSame(@NonNull final Tile tile) {
    return mSerialNum == tile.getSerialNum();
  }

  void openImage() {
    mImageId = changeImageId(mSerialNum);
  }

  void closeImage() {
    mImageId = changeImageId(0);
  }
}
