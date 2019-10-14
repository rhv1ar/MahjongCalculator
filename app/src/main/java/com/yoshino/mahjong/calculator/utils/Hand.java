package com.yoshino.mahjong.calculator.utils;

import static com.yoshino.mahjong.calculator.MyException.TileShortageException;

import java.io.Serializable;


public class Hand extends Tiles implements Serializable {
  private static final String TAG = Hand.class.getSimpleName();

  public Hand(final String name) {
    super(name, 14);
  }

  @Override
  public Tile removeTile() throws TileShortageException {
    for (int i = 0; i < this.size(); i++) {
      Tile tile = get(i);
      if (tile.isSelected()) {
        remove(tile);
        if (size() > 0) {
          selectTile((i == 0) ? i : i - 1);
        }
        return tile;
      }
    }
    throw new TileShortageException("There is no selected tile in hand");
  }

  public void selectTile(final int index) {
    for (int i = 0; i < size(); i++) {
      Tile tile = get(i);
      if (i == index) {
        tile.setSelected(true);
      } else {
        tile.setSelected(false);
      }
    }
  }
}
