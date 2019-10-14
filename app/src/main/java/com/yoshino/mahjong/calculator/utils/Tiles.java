package com.yoshino.mahjong.calculator.utils;

import android.util.Log;

import com.yoshino.mahjong.calculator.MyException;
import com.yoshino.mahjong.calculator.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Tiles extends ArrayList<Tile> implements Serializable {
  private final static String TAG = Tiles.class.getSimpleName();
  private String mName;
  private int mLimit;

  Tiles(final String name, final int limit) {
    mName = name;
    mLimit = limit;
  }

  public int[] toSerialNumArray() {
    int[] array = new int[38];
    for (Tile tile : this) {
      array[tile.getSerialNum()]++;
    }
    return array;
  }

  public boolean addTile(final Tile tile) {
    Log.v(TAG, tile.getName() + " is added to " + getName());
    if (size() == mLimit) {
      Log.v(TAG,  mName + " has already " + mLimit+ " tiles");
      return false;
    }

    add(tile);
    return true;
  }

  public abstract Tile removeTile() throws MyException.TileShortageException;

  public String getName() {
    return mName;
  }

  public List<Integer> getIds() {
    List<Integer> list = new ArrayList<>();
    for (Tile tile : this) {
      list.add(tile.getImageId());
    }
    return list;
  }

  public List<Integer> getHeightPositions() {
    List<Integer> list = new ArrayList<>();
    for (Tile tile : this) {
      list.add(tile.getHeightPosition());
    }
    return list;
  }

}
