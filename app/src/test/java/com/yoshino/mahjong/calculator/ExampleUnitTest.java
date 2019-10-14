package com.yoshino.mahjong.calculator;

import android.util.Log;

import com.yoshino.mahjong.calculator.utils.PointTable;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String TAG = ExampleUnitTest.class.getSimpleName();
    @Test
    public void addition_isCorrect() throws Exception {

        for (int fan = 1; fan < 13; fan++) {
            for (int fu = 20; fu <=110; fu += 10) {
                PointTable p = new PointTable(0, fan, fu);
                Log.v(TAG, fan + "翻" + fu + "符");
                Log.v(TAG, "親：" + p.ron(true) + " 子：" + p.ron(false));
            }
        }

        assertEquals(4, 2 + 2);
    }
}