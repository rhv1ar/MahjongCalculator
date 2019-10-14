package com.yoshino.mahjong.calculator.utils;

import com.yoshino.mahjong.calculator.models.BoardModel;
import com.yoshino.mahjong.calculator.models.PlayerModel;
import com.yoshino.mahjong.calculator.statics.Wind;
import com.yoshino.mahjong.calculator.models.FanModel;

import java.util.ArrayList;


public class FanCalculator extends ArrayList<FanModel> {
  private final String TAG = FanCalculator.class.getSimpleName();

  private Combination mCombination;
  private int mDora;
  private boolean mIsTsumo;
  private boolean mIsParent;
  private boolean mIsReach;
  private boolean mIsDoubleReach;
  private boolean mIsFirstGoAround;
  private boolean mIsFirstTurnWin;
  private boolean mIsFinalTileWin;
  private boolean mIsChankan;
  private boolean mIsRinsyan;
  private Wind mSeatWind;
  private Wind mRoundWind;

  FanCalculator(final Combination comb, final PlayerModel playerModel, final BoardModel boardModel) {
    mCombination = comb;
    mDora = playerModel.getDora();
    mIsParent = playerModel.isParent();
    mIsReach = playerModel.isReach();
    mIsDoubleReach = playerModel.isDoubleReach();
    mIsFirstGoAround = playerModel.isFirstGoAround();
    mIsFirstTurnWin = playerModel.isFirstTurnWin();
    mIsFinalTileWin = playerModel.isFinalTileWin();
    mIsChankan = playerModel.isChankan();
    mIsRinsyan = playerModel.isRinsyan();
    mSeatWind = playerModel.getWind();
    mRoundWind = boardModel.getWind();
  }
  public FanCalculator() {}

  boolean hasPeace() {
    for (FanModel fan : this) {
      if (fan.getName().equals("平和")) {
        return true;
      }
    }
    return false;
  }

  boolean hasYakuman() {
    for (FanModel fanModel : this) {
      if (fanModel.isYakuman()) {
        return true;
      }
    }
    return false;
  }

  int getSumYakuman() {
    int count = 0;
    for (FanModel fan : this) {
      if (fan.isYakuman()) {
        count++;
      }
    }
    return count;
  }

  public int getSumFan() {
    int count = 0;
    for (FanModel fan : this) {
      count += fan.getNum();
    }
    return count;
  }

  void calculate(final boolean isTsumo) {
    mIsTsumo = isTsumo;

    switch (mCombination.getType()) {
      case NORMAL:
        blessing();
        fourConcealedTriples();
        fourQuads();
        allHoners();
        bigFourWinds();
        smallFourWinds();
        bigDragons();
        allGreen();
        bigWheel();
        allTerminals();
        nineTreasures();

        if (!hasYakuman()) {
          concealedSelfDraw();
          doubleReach();
          reach();
          firstGoAround();
          allSimples();
          peace();
          seatWind();
          roundWind();
          dragons();
          finalTileWin();
          robbingQuad();
          afterQuad();
          threeColorRuns();
          threeColorTriples();
          fullStraight();
          fullFlush();
          halfFlush();
          littleDragons();
          allTriples();
          threeQuads();
          twoDoubleRuns();
          doubleRun();
          pureOutsideHand();
          allTerminalsAndHonors();
          mixedOutsideHand();
          threeConcealedTriples();
          dora();
        }
        break;
      case CHITOITSU:
        sevenPairs();
        //blessing();
        allHoners();
        if (!hasYakuman()) {
          concealedSelfDraw();
          doubleReach();
          reach();
          firstGoAround();
          allSimples();
          robbingQuad();
          afterQuad();
          finalTileWin();
          allTerminalsAndHonors();
          fullFlush();
          halfFlush();
          dora();
        }
        break;
      case KOKUSHIMUSOU:
        thirteenOrphans();
        //blessing();
        break;
    }
  }


  // 立直
  private void reach() {
    if (containFan("ダブル立直")) {
      return;
    }
    if (mIsReach) {
      add(new FanModel(1, "立直"));
    }
  }

  // 一発
  private void firstGoAround() {
    if (mIsFirstGoAround) {
      add(new FanModel(1, "一発"));
    }
  }

  // 自風
  private void seatWind() {
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getType()) {
        case KAN_DARK:
        case KAN_LIGHT:
        case PON:
        case KOHTSU:
          if (mentsu.getFirstTile().getName().equals(mSeatWind.toString())) {
            add(new FanModel(1, "自風 " + mSeatWind.toString()));
          }
          break;
        default:
          break;
      }
    }
  }

  // 場風
  private void roundWind() {
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getType()) {
        case KAN_DARK:
        case KAN_LIGHT:
        case PON:
        case KOHTSU:
          if (mentsu.getFirstTile().getName().equals(mRoundWind.toString())) {
            add(new FanModel(1, "場風 " + mRoundWind));
          }
          break;
        default:
          break;
      }
    }
  }

  // 役牌
  private void dragons() {
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getType()) {
        case KOHTSU:
        case PON:
        case KAN_LIGHT:
        case KAN_DARK:
          Tile tile = mentsu.getFirstTile();
          if (tile.isDragon()) {
            add(new FanModel(1, "役牌 " + tile.getName()));
          }
          break;
        default:
          break;
      }
    }
  }

  // 断幺
  private void allSimples() {
    for (Tile tile : mCombination.getTileList()) {
      if (tile.isCorner()) {
        return;
      }
    }

    add(new FanModel(1, "断幺"));
  }

  // 平和
  private void peace() {
    boolean check = false;
    int count = 0;
    Tile head = mCombination.getHead();
    Tile hora = mCombination.getHoraTile();

    if (head.isDragon()
            || mSeatWind.toString().equals(head.getName())
            || mRoundWind.toString().equals(head.getName())) {
      return;
    }

    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getType()) {
        case SHUNTSU:
          if (mentsu.getFirstTile().isCorner()) {
            if (mentsu.getFirstTile().equalTile(hora)) {
              check = true;
            }
          } else if (mentsu.get(2).isCorner()) {
            if (mentsu.get(2).equalTile(hora)) {
              check = true;
            }
          } else {
            if (mentsu.getFirstTile().equalTile(hora) || mentsu.get(2).equalTile(hora)) {
              check = true;
            }
          }
          count++;
          break;
        case HEAD:
          break;
        default:
          return;
      }
    }

    if (count == 4 && check) {
      add(new FanModel(1, "平和"));
    }
  }

  // 門前清自摸和
  private void concealedSelfDraw() {
    if (!mCombination.isFuro() && mIsTsumo) {
      add(new FanModel(1, "門前清自摸和"));
    }
  }

  // 一盃口
  private void doubleRun() {
    if (containFan("二盃口")) {
      return;
    }

    int[] array = new int[38];
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      if (mentsu.getType() == Mentsu.Type.SHUNTSU) {
        array[mentsu.getFirstTile().getSerialNum()]++;
      }
    }

    for (int num : array) {
      if (!mCombination.isFuro() && num >= 2) {
        add(new FanModel(1, "一盃口"));
      }
    }
  }

  // 海底撈月 or 河底撈魚
  private void finalTileWin() {
    if (!mIsFinalTileWin) {
      return;
    }
    if (mIsTsumo) {
      add(new FanModel(1, "海底撈月"));
    } else {
      add(new FanModel(1, "河底撈魚"));
    }
  }


  // 嶺上開花
  private void afterQuad() {
    if (mIsRinsyan) {
      add(new FanModel(1, "嶺上開花"));
    }
  }

  // ダブル立直
  private void doubleReach() {
    if (mIsDoubleReach) {
      add(new FanModel(2, "ダブル立直"));
    }
  }

  // 搶槓
  private void robbingQuad() {
    if (mIsChankan) {
      add(new FanModel(1, "搶槓"));
    }
  }

  // 対々和
  private void allTriples() {
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getType()) {
        case HEAD:
        case KOHTSU:
        case PON:
        case KAN_LIGHT:
        case KAN_DARK:
          break;
        default:
          return;
      }
    }

    add(new FanModel(2, "対々和"));
  }

  // 三色同順
  private void threeColorRuns() {
    int[] array = new int[38];
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      for (Tile tile : mentsu) {
        switch (mentsu.getType()) {
          case SHUNTSU:
          case CHEW:
            array[tile.getSerialNum()] = 1;
            break;
          default:
            break;
        }
      }
    }

    for (int i = 11; i < 30; i++) {
      if (array[i] == 1) {
        array[i % 10]++;
      }
    }
    for (int i = 0; i < 10; i++) {
      if (array[i] == 3) {
        if (mCombination.isFuro()) {
          add(new FanModel(1, "三色同順"));
        } else {
          add(new FanModel(2, "三色同順"));
        }
        return;
      }
    }

  }

  // 三色同刻
  private void threeColorTriples() {
    int[] array = new int[38];
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      for (Tile tile : mentsu) {
        switch (mentsu.getType()) {
          case KOHTSU:
          case PON:
          case KAN_DARK:
          case KAN_LIGHT:
            array[tile.getSerialNum()] = 1;
            break;
          default:
            break;
        }
      }
    }
    for (int i = 11; i < 30; i++) {
      if (array[i] == 1) {
        array[i % 10]++;
      }
    }
    for (int i = 0; i < 10; i++) {
      if (array[i] == 3) {
        add(new FanModel(2, "三色同刻"));
        return;
      }
    }
  }

  // 一気通貫
  private void fullStraight() {
    int[] array = new int[38];
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getType()) {
        case SHUNTSU:
        case CHEW:
          array[mentsu.getFirstTile().getSerialNum()] = 1;
          break;
        default:
          break;
      }
    }

    if ((array[1] == 1 && array[4] == 1 && array[7] == 1)
            || (array[11] == 1 && array[14] == 1 && array[17] == 1)
            || (array[21] == 1 && array[24] == 1 && array[27] == 1)) {
      if (mCombination.isFuro()) {
        add(new FanModel(1, "一気通貫"));
      } else {
        add(new FanModel(2, "一気通貫"));
      }
    }
  }

  // 七対子
  private void sevenPairs() {
    add(new FanModel(2, "七対子"));
  }

  // 混全帯公九
  private void mixedOutsideHand() {
    if (containFan("混老頭", "純全帯公九")) {
      return;
    }
    int count = 0;

    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getType()) {
        case HEAD:
        case TOITSU:
        case KOHTSU:
        case PON:
        case KAN_LIGHT:
        case KAN_DARK:
          if (mentsu.getFirstTile().isCorner()) {
            count++;
          }
          break;
        case SHUNTSU:
        case CHEW:
          if (mentsu.getFirstTile().isCorner() || mentsu.getLastTile().isCorner()) {
            count++;
          }
          break;
        default:
          break;
      }
    }

    if (count == 5) {
      if (mCombination.isFuro()) {
        add(new FanModel(1, "混全帯公九"));
      } else {
        add(new FanModel(2, "混全帯公九"));
      }
    }
  }

  // 三暗刻
  private void threeConcealedTriples() {
    int count = 0;
    boolean check = false;

    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getType()) {
        case KOHTSU:
        case KAN_DARK:
          count++;
          break;
        case SHUNTSU:
          for (Tile tile : mentsu) {
            if (tile.equalTile(mCombination.getHoraTile())) {
              check = true;
            }
          }
          break;
        default:
          break;
      }
    }

    if (count == 4) {
      add(new FanModel(2, "三暗刻"));
    }
    if (count == 3) {
      if (mIsTsumo) {
        add(new FanModel(2, "三暗刻"));
      } else {
        // 単騎 or 両面 or 辺張 or 嵌張
        if (mCombination.getHead().equalTile(mCombination.getHoraTile()) || check) {
          add(new FanModel(2, "三暗刻"));
        }
      }
    }
  }

  // 小三元
  private void littleDragons() {
    boolean head = false;
    int count = 0;

    for (Mentsu mentsu : mCombination.getMentsuList()) {
      if (mentsu.getFirstTile().isDragon()) {
        switch (mentsu.getType()) {
          case HEAD:
            head = true;
            break;
          case KOHTSU:
          case PON:
          case KAN_LIGHT:
          case KAN_DARK:
            count++;
            break;
          default:
            break;
        }
      }
    }

    if (head && count == 2) {
      add(new FanModel(2, "小三元"));
    }
  }

  // 混老頭
  private void allTerminalsAndHonors() {
    for (Tile tile : mCombination.getTileList()) {
      if (!tile.isCorner()) {
        return;
      }
    }

    add(new FanModel(2, "混老頭"));
  }

  // 三槓子
  private void threeQuads() {
    if (containFan("四槓子")) {
      return;
    }

    int count = 0;
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getType()) {
        case KAN_LIGHT:
        case KAN_DARK:
          count++;
          break;
        default:
          break;
      }
    }
    if (count == 3) {
      add(new FanModel(2, "三槓子"));
    }
  }

  // 純全帯公九
  private void pureOutsideHand() {
    int count = 0;
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      if (!mentsu.getFirstTile().isHonor() && mentsu.getFirstTile().isCorner()) {
        count++;
      } else if (!mentsu.getLastTile().isHonor() && mentsu.getLastTile().isCorner()) {
        count++;
      }
    }

    if (count == 5) {
      if (mCombination.isFuro()) {
        add(new FanModel(2, "純全帯公九"));
      } else {
        add(new FanModel(3, "純全帯公九"));
      }
    }
  }

  // 二盃口
  private void twoDoubleRuns() {
    int count = 0;
    int[] array = new int[38];
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      if (mentsu.getType() == Mentsu.Type.SHUNTSU) {
        array[mentsu.getFirstTile().getSerialNum()]++;
      }
    }

    for (int num : array) {
      if (num == 4) {
        add(new FanModel(3, "二盃口"));
        return;
      }

      if (num >= 2) {
        count++;
      }
    }

    if (count == 2 && !mCombination.isFuro()) {
      add(new FanModel(3, "二盃口"));
    }
  }

  // 混一色
  private void halfFlush() {
    if (containFan("清一色", "字一色")) {
      return;
    }
    int m = 0, s = 0, p = 0, l = 0;
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getFirstTile().getType()) {
        case MANZU:
          m = 1;
          break;
        case SOUZU:
          s = 1;
          break;
        case PINZU:
          p = 1;
          break;
        case WIND:
        case DRAGON:
          l = 1;
          break;
        default:
          break;
      }
    }
    if (l == 1) {
      if (m + s + p == 1) {
        if (mCombination.isFuro()) {
          add(new FanModel(2, "混一色"));
        } else {
          add(new FanModel(3, "混一色"));
        }
      }
    }
  }

  // 清一色
  private void fullFlush() {

    Tile.Type preType = mCombination.getMentsuList().get(0).getFirstTile().getType();
    for (Mentsu mentsu : mCombination.getMentsuList()) {
      Tile.Type type = mentsu.getFirstTile().getType();
      switch (type) {
        case WIND:
        case DRAGON:
          return;
        default:
          if (preType != type) {
            return;
          }
          break;
      }
    }

    if (mCombination.isFuro()) {
      add(new FanModel(5, "清一色"));
    } else {
      add(new FanModel(6, "清一色"));
    }
  }

  // 四暗刻
  private void fourConcealedTriples() {
    int count = 0;

    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getType()) {
        case KOHTSU:
        case KAN_DARK:
          count++;
          break;
        default:
          break;
      }
    }

    if (count == 4) {
      if (mIsTsumo || mCombination.getHead().equalTile(mCombination.getHoraTile())) {
        add(new FanModel("四暗刻"));
      }
    }
  }

  // 国士無双
  private void thirteenOrphans() {
    add(new FanModel("国士無双"));
  }

  // 大三元
  private void bigDragons() {
    int count = 0;

    for (Mentsu mentsu : mCombination.getMentsuList()) {
      if (mentsu.getFirstTile().isDragon()) {
        switch (mentsu.getType()) {
          case KOHTSU:
          case KAN_LIGHT:
          case KAN_DARK:
            count++;
            break;
          default:
        }
      }
    }

    if (count == 3) {
      add(new FanModel("大三元"));
    }
  }

  // 小四喜
  private void smallFourWinds() {
    if (containFan("大四喜")) {
      return;
    }

    int count = 0;
    boolean head = false;

    for (Mentsu mentsu : mCombination.getMentsuList()) {
      if (mentsu.getFirstTile().isWind()) {
        switch (mentsu.getType()) {
          case KAN_DARK:
          case KAN_LIGHT:
          case KOHTSU:
            count++;
            break;
          case HEAD:
            head = true;
            break;
          default:
            break;
        }
      }
    }

    if (head && count == 3) {
      add(new FanModel("小四喜"));
    }
  }

  // 大四喜
  private void bigFourWinds() {
    int count = 0;

    for (Mentsu mentsu : mCombination.getMentsuList()) {
      if (mentsu.getFirstTile().isWind()) {
        switch (mentsu.getType()) {
          case KOHTSU:
          case KAN_LIGHT:
          case KAN_DARK:
            count++;
            break;
          default:
            break;
        }
      }
    }

    if (count == 4) {
      add(new FanModel("大四喜"));
    }
  }

  // 字一色
  private void allHoners() {
    for (Tile tile : mCombination.getTileList()) {
      switch (tile.getType()) {
        case MANZU:
        case SOUZU:
        case PINZU:
          return;
        default:
          break;
      }
    }

    add(new FanModel("字一色"));
  }

  // 清老頭
  private void allTerminals() {
    for (Tile tile : mCombination.getTileList()) {
      switch (tile.getType()) {
        case MANZU:
        case PINZU:
        case SOUZU:
          if (!tile.isCorner()) {
            return;
          }
          break;
        default:
          return;
      }
    }

    add(new FanModel("清老頭"));
  }

  // 地和 or 天和
  private void blessing() {
    if (mIsTsumo && mIsFirstTurnWin) {
      if (mIsParent) {
        add(new FanModel("天和"));
      } else {
        add(new FanModel("地和"));
      }
    }
  }

  // 緑一色
  private void allGreen() {
    for (Tile tile : mCombination.getTileList()) {
      if (!tile.isGreen()) {
        return;
      }
    }

    add(new FanModel("緑一色"));
  }

  // 九連宝燈
  private void nineTreasures() {
    if (mCombination.isFuro()) {
      return;
    }

    int[] hand = new int[38];
    for (Tile tile : mCombination.getTileList()) {
      switch (tile.getType()) {
        case MANZU:
        case SOUZU:
        case PINZU:
          hand[tile.getSerialNum()]++;
          break;
        default:
          return;
      }
    }

    for (int i = 1; i < 30; i += 10) {
      int count = 0;
      for (int j = i; j < i + 9; j++) {
        count = count + hand[j];
      }
      if (hand[i] >= 3 && hand[i + 1] >= 1 && hand[i + 2] >= 1 && hand[i + 3] >= 1 && hand[i + 4] >= 1
              && hand[i + 5] >= 1 && hand[i + 6] >= 1 && hand[i + 7] >= 1 && hand[i + 8] >= 3) {
        if (count == 14) {
          add(new FanModel("九連宝燈"));
        }
      }
    }
  }

  // 四槓子
  private void fourQuads() {
    int count = 0;

    for (Mentsu mentsu : mCombination.getMentsuList()) {
      switch (mentsu.getType()) {
        case KAN_LIGHT:
        case KAN_DARK:
          count++;
          break;
        default:
          break;
      }
    }

    if (count == 4) {
      add(new FanModel("四槓子"));
    }
  }

  //大車輪
  private void bigWheel() {
    for (Tile tile : mCombination.getTileList()) {
      if (!tile.isWheel()) {
        return;
      }
    }
    add(new FanModel("大車輪"));
  }

  // ドラ
  private void dora() {
    if (mDora > 0) {
      add(new FanModel(mDora, "ドラ"));
    }
  }

  private boolean containFan(final String... fanNames) {
    for (FanModel fan : this) {
      for (String fanName : fanNames) {
        if (fan.getName().equals(fanName)) {
          return true;
        }
      }
    }
    return false;
  }
}
