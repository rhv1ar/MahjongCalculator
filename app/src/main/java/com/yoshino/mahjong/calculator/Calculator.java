package com.yoshino.mahjong.calculator;

import android.util.Log;

import java.util.ArrayList;
import com.yoshino.mahjong.calculator.models.BoardModel;
import com.yoshino.mahjong.calculator.utils.Combination;
import com.yoshino.mahjong.calculator.utils.Call;
import com.yoshino.mahjong.calculator.utils.FanCalculator;
import com.yoshino.mahjong.calculator.utils.FuCalculator;
import com.yoshino.mahjong.calculator.utils.Hand;
import com.yoshino.mahjong.calculator.utils.Mentsu;
import com.yoshino.mahjong.calculator.models.PlayerModel;
import com.yoshino.mahjong.calculator.utils.Tile;

import static com.yoshino.mahjong.calculator.MyException.*;


public class Calculator extends ArrayList<Combination> {
  /**
   * Class tag.
   */
  private static final String TAG = Calculator.class.getSimpleName();

  private BoardModel mBoardModel;
  private PlayerModel mPlayerModel;

  private Combination mTsumoComb, mRonComb;
  public Combination getTsumoResult() {
    return mTsumoComb;
  }
  public Combination getRonResult() {
    return mRonComb;
  }

  public Calculator(final BoardModel boardModel, final PlayerModel playerModel) {
    mBoardModel = boardModel;
    mPlayerModel = playerModel;
  }

  public void calculate() throws InvalidCallCaseException {
    clear();
    Hand hand = mPlayerModel.getHand();
    ArrayList<Call> callList = mPlayerModel.getCalls();
    if (hand.size() + callList.size() * 3 != 14) {
      return;
    }
    Tile horaTile = mPlayerModel.getSelectedTile();
    if (horaTile == null) {
      return;
    }

    pickNormal(hand.toSerialNumArray(), callList, horaTile);
    pick7Pairs(hand.toSerialNumArray(), horaTile);
    pick13Orphans(hand.toSerialNumArray(), horaTile);

    for (Combination comb : this) {
      mTsumoComb = null;
      mRonComb = null;
      Combination tsumoComb = comb.calculate(mPlayerModel, mBoardModel, true);
      Combination ronComb = comb.calculate(mPlayerModel, mBoardModel, false);

      if (mTsumoComb == null) {
        mTsumoComb = tsumoComb;
      } else {
        if (mTsumoComb.compareCombination(tsumoComb)) {
          mTsumoComb = tsumoComb;
        }
      }
      if (mRonComb == null) {
        mRonComb = ronComb;
      } else {
        if (mRonComb.compareCombination(ronComb)) {
          mRonComb = ronComb;
        }
      }
    }

    mTsumoComb.print();
    mRonComb.print();
  }



  private void pick7Pairs(final int[] hand, final Tile horaTile) {
    Combination comb = new Combination(Combination.Type.CHITOITSU);
    comb.setHoraTile(horaTile);
    for (int i = 0; i < hand.length; i++) {
      if (hand[i] == 2) {
        comb.addMentsu(new Mentsu(i, Mentsu.Type.TOITSU));
      }
    }
    if (comb.mentsuSize() == 7) {
      add(comb);
    }
  }

  private void pick13Orphans(final int[] hand, final Tile horaTile) {
    Combination comb = new Combination(Combination.Type.KOKUSHIMUSOU);
    comb.setHoraTile(horaTile);
    for (int i = 0; i < hand.length; i++) {
      if (i % 10 == 1 || i % 10 == 9 || i >= 31) {
        if (hand[i] == 0) {
          return;
        }
        for (int j = 0; j < hand[i]; j++) {
          comb.addMentsu(new Mentsu(i, Mentsu.Type.HAND));
        }
      }
    }

    if (comb.mentsuSize() == 14) {
      add(comb);
    }
  }

  private void pickNormal(final int[] hand, final ArrayList<Call> callList, final Tile horaTile) {
    for(int head = 0; head < 38; head++) {
      int[] handTemp = hand.clone();
      // find head in hand
      if (handTemp[head] >= 2) {
        handTemp[head] -= 2;
        Combination comb = new Combination(Combination.Type.NORMAL);
        comb.setHoraTile(horaTile);
        comb.addCalls(callList);
        comb.addMentsu(new Mentsu(head, Mentsu.Type.HEAD));
        for (int i = 0; i <= 4; i++) {
          pickRecursiveKohtsu(comb, handTemp, i, 1);
        }
      }
    }
  }

  private void pickRecursiveKohtsu(final Combination comb, final int[] hand, final int loop, final int next) {
    if (loop == 0) {
      pickRecursiveShuntsu(comb, hand, 1);
      return;
    }

    for (int i = next; i < hand.length; i++) {
      if (hand[i] >= 3) {
        Combination combTemp = (Combination) comb.clone();
        combTemp.addMentsu(new Mentsu(i, Mentsu.Type.KOHTSU));
        int[] handTemp = hand.clone();
        handTemp[i] -= 3;
        pickRecursiveKohtsu(combTemp, handTemp, loop - 1, i + 1);
      }
    }
  }

  private void pickRecursiveShuntsu(final Combination comb, final int[] hand, final int next) {

    if (comb.mentsuSize() == 5) {
      add(comb);
      return;
    }

    for (int i = next; i <= 27; i++) {
      if (hand[i] >= 1 && hand[i + 1] >= 1 && hand[i + 2] >= 1) {
        Combination combTemp = (Combination) comb.clone();
        combTemp.addMentsu(new Mentsu(i, Mentsu.Type.SHUNTSU));
        int[] handTemp = hand.clone();
        handTemp[i]--;
        handTemp[i + 1]--;
        handTemp[i + 2]--;
        pickRecursiveShuntsu(combTemp, handTemp, i);
      }
    }

    // 向聴数計算
        /*if (horaPattern == PATTERN_SYANTEN) {
            judge3(observableHand, mentsu);
        }*/

  }

  //向聴数を返す
  //private int checkSyanten(/*MainViewModel player, */String observableRoundWind, int[] observableHand) {
    /*private int checkSyanten() {
        int[] array = mHand.convert2Array();
        int chitoitsu = 6;
        int kokushi = 13;
        mSyanten.set(8);

        //七対子
        int count = 0;
        for (int i = 0; i < 38; i++) {
            if (array[i] > 0) {
                count++;
            }
            if (array[i] >= 2) {
                chitoitsu--;
            }
        }
        if (count >= 7) {
            count = 7;
        }
        chitoitsu = chitoitsu + 7 - count;


        //国士無双
        for (int i = 0; i < 38; i++) {
            if (array[i] >= 1 && (i % 10 == 1 || i % 10 == 9 || i >= 31)) {
                kokushi--;
            }
        }
        for (int i = 0; i < 38; i++) {
            if (array[i] >= 2 && (i % 10 == 1 || i % 10 == 9 || i >= 31)) {
                kokushi--;
                break;
            }
        }

        ArrayList<Integer> mentsu = new  ArrayList<>();
        judge(mentsu, 0, 5, null, array);

        int min = 10;
        if (min > kokushi) {
            min = kokushi;
        }
        if (min > chitoitsu) {
            min = chitoitsu;
        }
        if (min > mSyanten.get()) {
            min = mSyanten.get();
        }


        return min;
    }
    private static void checkReachHai(final MainViewModel player, final String observableRoundWind) {
        player.arrayReach = new ArrayList<>();
        ArrayList<Tile> copy;
        int[] observableHand;
        Tile h;
        int size = player.observableHand.size();

        for (int i = 0; i < size; i++) {
            copy = new ArrayList<>(player.observableHand);
            h = copy.remove(i);
            observableHand = checkCount(copy);
            if (checkSyanten(player, observableRoundWind, observableHand) == 0) {
                player.arrayReach.add(h);
            }

        }
    }*/
}