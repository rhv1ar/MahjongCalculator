package com.yoshino.mahjong.calculator.models;

import android.util.Log;

import com.yoshino.mahjong.calculator.R;
import com.yoshino.mahjong.calculator.statics.Wind;
import com.yoshino.mahjong.calculator.utils.Call;
import com.yoshino.mahjong.calculator.utils.Hand;
import com.yoshino.mahjong.calculator.utils.Tile;
import com.yoshino.mahjong.calculator.utils.Tiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static com.yoshino.mahjong.calculator.MyException.*;

public class PlayerModel implements Serializable {
  private static String TAG = PlayerModel.class.getSimpleName();

  private final BehaviorSubject<Tiles> hand = BehaviorSubject.createDefault(new Hand("hand"));
  private final BehaviorSubject<Tiles> call1 = BehaviorSubject.createDefault(new Call("call_1"));
  private final BehaviorSubject<Tiles> call2 = BehaviorSubject.createDefault(new Call("call_2"));
  private final BehaviorSubject<Tiles> call3 = BehaviorSubject.createDefault(new Call("call_3"));
  private final BehaviorSubject<Tiles> call4 = BehaviorSubject.createDefault(new Call("call_4"));
  private final BehaviorSubject<BehaviorSubject<Tiles>> selectedArea = BehaviorSubject.createDefault(hand);
  private final BehaviorSubject<Wind> wind = BehaviorSubject.createDefault(Wind.EAST);

  private final BehaviorSubject<Integer> dora = BehaviorSubject.createDefault(0);

  private final BehaviorSubject<Boolean> isTsumo = BehaviorSubject.createDefault(true);
  private final BehaviorSubject<Boolean> isReach = BehaviorSubject.createDefault(false);
  private final BehaviorSubject<Boolean> isDoubleReach = BehaviorSubject.createDefault(false);
  private final BehaviorSubject<Boolean> isFirstGoAround = BehaviorSubject.createDefault(false);
  private final BehaviorSubject<Boolean> isFirstTurnWin = BehaviorSubject.createDefault(false);
  private final BehaviorSubject<Boolean> isFinalTileWin = BehaviorSubject.createDefault(false);
  private final BehaviorSubject<Boolean> isChankan = BehaviorSubject.createDefault(false);
  private final BehaviorSubject<Boolean> isRinsyan = BehaviorSubject.createDefault(false);

  public final Observable<List<Integer>> formattedHand;
  public final Observable<List<Integer>> formattedCall1;
  public final Observable<List<Integer>> formattedCall2;
  public final Observable<List<Integer>> formattedCall3;
  public final Observable<List<Integer>> formattedCall4;

  public final Observable<List<Integer>> formattedHandHPos;

  public final Observable<Integer> formattedKanTile;

  public final Observable<Integer> formattedHandTextColor;
  public final Observable<Integer> formattedCall1TextColor;
  public final Observable<Integer> formattedCall2TextColor;
  public final Observable<Integer> formattedCall3TextColor;
  public final Observable<Integer> formattedCall4TextColor;

  public final Observable<String> formattedWind;

  public final Observable<Integer> formattedReachBgColor;
  public final Observable<Integer> formattedDoubleReachBgColor;
  public final Observable<Integer> formattedFirstGoAround;
  public final Observable<Integer> formattedFinalTileWinBgColor;
  public final Observable<Integer> formattedRinsyanBgColor;

  public final Observable<String> formattedDora;

  public void selectHand() {
    selectedArea.onNext(hand);
  }
  public void selectCall1() {
    selectedArea.onNext(call1);
  }
  public void selectCall2() {
    selectedArea.onNext(call2);
  }
  public void selectCall3() {
    selectedArea.onNext(call3);
  }
  public void selectCall4() {
    selectedArea.onNext(call4);
  }

  private Integer getTextColor(final Tiles area, final String name) {
    return (area.getName().equals(name)) ? R.color.black : R.color.gray;
  }
  private Integer getBackGroundColor(final boolean isSelected) {
    return isSelected ? R.color.blue : R.color.gray;
  }

  //private Integer getBgColor(final )

  public PlayerModel() {
    // format player's tiles to id
    formattedHand = hand.map(Tiles::getIds);
    formattedCall1 = call1.map(Tiles::getIds);
    formattedCall2 = call2.map(Tiles::getIds);
    formattedCall3 = call3.map(Tiles::getIds);
    formattedCall4 = call4.map(Tiles::getIds);

    // format player's hand tile height position
    formattedHandHPos = hand.map(Tiles::getHeightPositions);

    // format area text color
    Observable<String> selectedAreaId = selectedArea.map(select -> select.getValue().getName());
    formattedHandTextColor = Observable.combineLatest(hand, selectedAreaId, this::getTextColor);
    formattedCall1TextColor = Observable.combineLatest(call1, selectedAreaId, this::getTextColor);
    formattedCall2TextColor = Observable.combineLatest(call2, selectedAreaId, this::getTextColor);
    formattedCall3TextColor = Observable.combineLatest(call3, selectedAreaId, this::getTextColor);
    formattedCall4TextColor = Observable.combineLatest(call4, selectedAreaId, this::getTextColor);

    // format image id for kan dialog
    formattedKanTile = selectedArea.map(select -> select.getValue().get(0).getImageId());

    // format wind to string
    formattedWind = wind.map(Wind::toString);

    // format reach to background color
    formattedReachBgColor = isReach.map(this::getBackGroundColor);
    // format double reach to background color
    formattedDoubleReachBgColor = isDoubleReach.map(this::getBackGroundColor);
    // format double reach to background color
    formattedFirstGoAround = isFirstGoAround.map(this::getBackGroundColor);
    // format final tile win to background color
    formattedFinalTileWinBgColor = isFinalTileWin.map(this::getBackGroundColor);
    // format rinsyan to background color
    formattedRinsyanBgColor = isRinsyan.map(this::getBackGroundColor);

    // format dora to string
    formattedDora = dora.map(Object::toString);
  }



  public Wind getWind() {
    return wind.getValue();
  }

  public int getDora() {
    return dora.getValue();
  }
  public void increaseDora() {
    dora.onNext(dora.getValue() + 1);
  }

  public boolean isTsumo() {
    return isTsumo.getValue();
  }
  public boolean isReach() {
    return isReach.getValue();
  }
  public boolean isDoubleReach() {
    return isDoubleReach.getValue();
  }
  public boolean isFirstGoAround() {
    return isFirstGoAround.getValue();
  }
  public boolean isFirstTurnWin() {
    return isFirstTurnWin.getValue();
  }
  public boolean isFinalTileWin() {
    return isFinalTileWin.getValue();
  }
  public boolean isChankan() {
    return isChankan.getValue();
  }
  public boolean isRinsyan() {
    return isRinsyan.getValue();
  }
  public void switchFinalTileWin() {
    isFinalTileWin.onNext(!isFinalTileWin.getValue());
    if (isFinalTileWin.getValue() && isRinsyan.getValue()) {
      switchRinsyan();
    }
  }
  public void switchRinsyan() {
    isRinsyan.onNext(!isRinsyan.getValue());
    if (isRinsyan.getValue()) {
      if (isFinalTileWin.getValue()) {
        isFinalTileWin.onNext(false);
      }
      if (isFirstGoAround.getValue()) {
        isFirstGoAround.onNext(false);
      }
    }
  }
  public void switchFirstGoAround() {
    if (!isReach.getValue() && !isDoubleReach.getValue()) {
      return;
    }
    isFirstGoAround.onNext(!isFirstGoAround.getValue());
    if (isRinsyan.getValue()) {
      isRinsyan.onNext(false);
    }
  }
  public void switchReach() {
    isReach.onNext(!isReach.getValue());
    if (isReach.getValue()) {
      if (isDoubleReach.getValue()) {
        isDoubleReach.onNext(false);
      }
    } else {
      if (!isDoubleReach.getValue()) {
        isFirstGoAround.onNext(false);
      }
    }
  }
  public void switchDoubleReach() {
    isDoubleReach.onNext(!isDoubleReach.getValue());
    if (isDoubleReach.getValue()) {
      if (isReach.getValue()) {
        isReach.onNext(false);
      }
    } else {
      if (!isReach.getValue()) {
        isFirstGoAround.onNext(false);
      }
    }
  }


  // 親
  public boolean isParent() {
    return wind.getValue().isParent();
  }

  public void initialize() {
    // initialize wind
    wind.onNext(Wind.EAST);

    // initialize hand
    hand.onNext(new Hand("hand"));

    // initialize calls
    call1.onNext(new Call("call_1"));
    call2.onNext(new Call("call_2"));
    call3.onNext(new Call("call_3"));
    call4.onNext(new Call("call_4"));

    // initialize flag
    isTsumo.onNext(true);
    isReach.onNext(false);
    isDoubleReach.onNext(false);
    isFirstGoAround.onNext(false);
    isFirstTurnWin.onNext(false);
    isFinalTileWin.onNext(false);
    isChankan.onNext(false);
    isRinsyan.onNext(false);
  }

  public Hand getHand() {
    return (Hand) hand.getValue();
  }

  public ArrayList<Call> getCalls() throws InvalidCallCaseException {
    return new ArrayList<Call>() {
      {
        if (((Call) call1.getValue()).isValid()) {
          add((Call) call1.getValue());
        }
        if (((Call) call2.getValue()).isValid()) {
          add((Call) call2.getValue());
        }
        if (((Call) call3.getValue()).isValid()) {
          add((Call) call3.getValue());
        }
        if (((Call) call4.getValue()).isValid()) {
          add((Call) call4.getValue());
        }
      }
    };
  }

  public void nextWind() {
    wind.onNext(wind.getValue().next());
  }


  public void addTile(final Tile target) {
    try {
      Tile tile = BoardModel.getInstance().getTile(target);
      Tiles area = selectedArea.getValue().getValue();
      if (!area.addTile(tile)) {
        BoardModel.getInstance().addTile(tile);
        return;
      }
      if (area instanceof Call) {
        ((Call) area).check();
      } else if (area instanceof Hand) {
        ((Hand) area).selectTile(area.size() - 1);
      }
      selectedArea.getValue().onNext(area);
    } catch (TileShortageException e) {
      Log.v(TAG, e.getMessage());
    }
  }

  public void removeTile() throws TileShortageException {
    Tiles area = selectedArea.getValue().getValue();
    Tile tile = area.removeTile();

    selectedArea.getValue().onNext(area);

    // return tile to yama
    tile.defaultViewParameters();
    BoardModel.getInstance().addTile(tile);
  }

  public void reverseKan() {
    Call call = (Call) selectedArea.getValue().getValue();
    switch (call.getType()) {
      case KAN_LIGHT:
      case KAN_DARK:
        call.alignSurface();
        break;
      default:
        Log.v(TAG, call.getName() + " is not light kan");
        break;
    }
    Log.v(TAG, call.getType().name());
    selectedArea.getValue().onNext(call);
  }

  public void selectTile(final int index) {
    ((Hand) hand.getValue()).selectTile(index);
    hand.onNext(hand.getValue());
  }

  public Tile getSelectedTile() {
    for (Tile tile : hand.getValue()) {
      if (tile.isSelected()) {
        return tile;
      }
    }
    return null;
  }



}
