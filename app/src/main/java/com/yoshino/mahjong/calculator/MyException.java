package com.yoshino.mahjong.calculator;

public class MyException {

    public static class TileShortageException extends Exception {
        public TileShortageException(final String msg) {
            super(msg);
        }
    }
    public static class InvalidCallCaseException extends Exception {
        public InvalidCallCaseException(final String msg) {
            super(msg);
        }
    }
}
