package com.yoshino.mahjong.calculator.utils;

import android.util.Log;

import com.yoshino.mahjong.calculator.MyException;

import static com.yoshino.mahjong.calculator.MyException.*;

public class Call extends Tiles {
  private static final String TAG = Call.class.getSimpleName();
  private Mentsu.Type mType = Mentsu.Type.EMPTY;
  public Mentsu.Type getType() {
    return mType;
  }

  public Call(final String name) {
    super(name, 4);
  }

  public void check() {
    int[] array = toSerialNumArray();

    switch (size()) {
      case 0:
        mType = Mentsu.Type.EMPTY;
        return;
      case 3:
        for (int i = 0; i < array.length; i++) {
          // case chew
          if (array[i] == 1) {
            if (i <= 27 && i % 10 <= 7) {
              if (array[i + 1] == 1 && array[i + 2] == 1) {
                mType = Mentsu.Type.CHEW;
                alignSurface();
                return;
              }
            }
          }

          // case pon
          if (array[i] == 3) {
            mType = Mentsu.Type.PON;
            alignSurface();
            return;
          }
        }
        break;
      case 4:
        // case kan
        for (int num : array) {
          if (num == 4) {
            mType = Mentsu.Type.KAN_LIGHT;
            return;
          }
        }
        break;
      default:
        break;
    }

    mType = Mentsu.Type.UNKNOWN;
  }

  public void alignSurface() {
    switch (mType) {
      case CHEW:
      case PON:
        get(0).openImage();
        get(1).openImage();
        get(2).openImage();
        break;
      case KAN_LIGHT:
        mType = Mentsu.Type.KAN_DARK;
        get(0).closeImage();
        get(3).closeImage();
        break;
      case KAN_DARK:
        mType = Mentsu.Type.KAN_LIGHT;
        get(0).openImage();
        get(3).openImage();
        break;
      default:
        break;
    }
  }

  public boolean isValid() throws InvalidCallCaseException {
    switch (mType) {
      case CHEW:
      case PON:
      case KAN_LIGHT:
      case KAN_DARK:
        return true;
      case EMPTY:
        return false;
      case UNKNOWN:
        throw new InvalidCallCaseException(getName() + " is invalid case");
      default:
        return false;
    }
  }

  @Override
  public Tile removeTile() throws TileShortageException {
    if (size() == 0) {
      throw new TileShortageException(getName() + " has no tile");
    }
    Tile tile = remove(size() - 1);
    check();
    return tile;
  }
}
