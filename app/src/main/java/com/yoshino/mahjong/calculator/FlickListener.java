package com.yoshino.mahjong.calculator;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.yoshino.mahjong.calculator.utils.Tile;

public class FlickListener implements View.OnTouchListener {
    private static final String TAG = FlickListener.class.getSimpleName();

    /**
     * フリックイベントのリスナー
     */
    public interface OnFlickListener {
        Tile[] onTouchDown(final View view);
        void onTouchUp(final Tile tile);
    }

    /**
     * フリック判定時の遊び
     * 小さいほど判定が敏感になる
     */
    private static final float DEFAULT_ROOM = 100;

    private final OnFlickListener mListener;

    private float mLastX;
    private float mLastY;

    public FlickListener(OnFlickListener listener) {
        mListener = listener;
    }

    private Tile[] mTiles = null;

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                gestureDetector.onTouchEvent(event);
                touchDown(event);
                mTiles = mListener.onTouchDown(view);
                break;
            case MotionEvent.ACTION_UP:
                touchOff(mTiles, event);
                view.performClick();
                break;
        }
        return true;
    }

    private final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            //Log.v(TAG, "Longpress detected");
        }
    });


    private void touchDown(MotionEvent event) {
        mLastX = event.getX();
        mLastY = event.getY();
    }

    private void touchOff(final Tile[] tiles, final MotionEvent event) {
        final float currentX = event.getX();
        final float currentY = event.getY();

        // x -> y の順で判定しているので、斜めにフリックした場合はLeft,Rightのイベントの方が優先される
        // Up,Downを優先したい場合は、条件判定の順序を入れ替えること
        if ((currentX + DEFAULT_ROOM) < mLastX) {
            mListener.onTouchUp(tiles.length > 3 ? tiles[3] : null);
        } else if (mLastX < (currentX - DEFAULT_ROOM)) {
            mListener.onTouchUp(tiles.length > 2 ? tiles[2] : null);
        } else if ((currentY + DEFAULT_ROOM) < mLastY) {
            mListener.onTouchUp(tiles.length > 1 ? tiles[1] : null);
        } else {
            mListener.onTouchUp(tiles.length > 0 ? tiles[0] : null);
        }
    }

}