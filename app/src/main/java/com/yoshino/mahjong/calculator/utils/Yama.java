package com.yoshino.mahjong.calculator.utils;

import com.yoshino.mahjong.calculator.MyException;

public class Yama extends Tiles {
    public Yama(String name, int limit) {
        super(name, limit);
    }

    @Override
    public Tile removeTile() throws MyException.TileShortageException {
        return null;
    }

    public void init() {
        for (int i = 1; i <= 37; i++) {
            if (i % 10 == 0) {
                continue;
            }
            for (int j = 0; j < 4; j++) {
                add(new Tile(i));
            }
        }
    }

    public Tile getTile(final Tile target) throws MyException.TileShortageException {

        for (Tile tile : this) {
            if (tile.isSame(target)) {
                if (remove(tile)) {
                    return tile;
                }
            }
        }
        throw new MyException.TileShortageException("There is no " + target.getName() + " in yama");
    }
}
