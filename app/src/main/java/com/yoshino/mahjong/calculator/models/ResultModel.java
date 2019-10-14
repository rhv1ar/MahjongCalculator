package com.yoshino.mahjong.calculator.models;

import com.yoshino.mahjong.calculator.utils.Combination;
import com.yoshino.mahjong.calculator.utils.FanCalculator;
import com.yoshino.mahjong.calculator.utils.FuCalculator;
import com.yoshino.mahjong.calculator.utils.PointTable;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import java.io.Serializable;
import java.util.ArrayList;



public class ResultModel implements Serializable {
  private static final String TAG = ResultModel.class.getSimpleName();

  private final BehaviorSubject<FanCalculator> fan = BehaviorSubject.create();
  private final BehaviorSubject<FuCalculator> fu = BehaviorSubject.create();
  private final BehaviorSubject<PointTable> pointTable = BehaviorSubject.create();

  public final Observable<String> formattedFan;
  public final Observable<String> formattedFu;
  public final Observable<String> formattedTotalPoint;
  public final Observable<String> formattedChildPoint;
  public final Observable<String> formattedParentPoint;

  public ResultModel() {
    formattedFan = fan.map(f -> String.valueOf(f.getSumFan()));
    formattedFu = fu.map(f -> String.valueOf(f.getSumFu()));
    formattedTotalPoint = pointTable.map(p ->
            String.valueOf(p.getTsumoPointFromParent() + p.getTsumoPointFromChild() * 2));
    formattedChildPoint = pointTable.map(p -> String.valueOf(p.getTsumoPointFromChild()));
    formattedParentPoint = pointTable.map(p -> String.valueOf(p.getTsumoPointFromParent()));
  }

  public void applyResult(final Combination combination) {
    if (combination == null) {
      return;
    }
    fan.onNext(combination.getFanCalculator());
    fu.onNext(combination.getFuCalculator());
    pointTable.onNext(combination.getPointTable());
  }

  public ArrayList<FanModel> getFans() {
    return fan.getValue();
  }
}
