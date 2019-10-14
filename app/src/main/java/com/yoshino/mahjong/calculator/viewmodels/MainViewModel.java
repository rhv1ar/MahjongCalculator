package com.yoshino.mahjong.calculator.viewmodels;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.yoshino.mahjong.calculator.Calculator;
import com.yoshino.mahjong.calculator.models.BoardModel;
import com.yoshino.mahjong.calculator.models.ResultModel;
import com.yoshino.mahjong.calculator.utils.Combination;
import com.yoshino.mahjong.calculator.models.PlayerModel;
import com.yoshino.mahjong.calculator.utils.Tile;
import com.yoshino.mahjong.calculator.views.MainActivityNavigation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.disposables.Disposable;
import jp.keita.kagurazaka.rxproperty.NoParameter;
import jp.keita.kagurazaka.rxproperty.ReadOnlyRxProperty;
import jp.keita.kagurazaka.rxproperty.RxCommand;

import static com.yoshino.mahjong.calculator.MyException.*;


public class MainViewModel {
  private static String TAG = MainViewModel.class.getSimpleName();

  @NonNull
  private final PlayerModel mPlayerModel;
  private final BoardModel mBoardModel = BoardModel.getInstance();
  private final ResultModel mTsumoResultModel, mRonResultModel;
  private final Calculator mCalculator;


  public final ReadOnlyRxProperty<List<Integer>> observableHand;
  public final ReadOnlyRxProperty<List<Integer>> observableCall1;
  public final ReadOnlyRxProperty<List<Integer>> observableCall2;
  public final ReadOnlyRxProperty<List<Integer>> observableCall3;
  public final ReadOnlyRxProperty<List<Integer>> observableCall4;
  public final ReadOnlyRxProperty<List<Integer>> observableHandHPos;
  public final ReadOnlyRxProperty<Integer> observableHandTextColor;
  public final ReadOnlyRxProperty<Integer> observableCall1TextColor;
  public final ReadOnlyRxProperty<Integer> observableCall2TextColor;
  public final ReadOnlyRxProperty<Integer> observableCall3TextColor;
  public final ReadOnlyRxProperty<Integer> observableCall4TextColor;
  public final ReadOnlyRxProperty<String> observableSeatWind;
  public final ReadOnlyRxProperty<String> observableRoundWind;
  public final ReadOnlyRxProperty<String> observableDora;
  public final ReadOnlyRxProperty<String> observableStick100;


  public final RxCommand<NoParameter> commandHand = new RxCommand<>();
  public final RxCommand<NoParameter> commandCall1 = new RxCommand<>();
  public final RxCommand<NoParameter> commandCall2 = new RxCommand<>();
  public final RxCommand<NoParameter> commandCall3 = new RxCommand<>();
  public final RxCommand<NoParameter> commandCall4 = new RxCommand<>();

  public final RxCommand<NoParameter> commandSeatWind = new RxCommand<>();
  public final RxCommand<NoParameter> commandRoundWind = new RxCommand<>();
  public final RxCommand<NoParameter> commandDora = new RxCommand<>();
  public final RxCommand<NoParameter> commandStick100 = new RxCommand<>();

  public final RxCommand<NoParameter> commandRemove = new RxCommand<>();

  public final RxCommand<Integer> command = new RxCommand<>();

  private MainActivityNavigation mNavigation;

  public MainViewModel(final MainActivityNavigation navigation, @NotNull final PlayerModel playerModel, final ResultModel tsumoResultModel, final ResultModel ronResultModel) {

    mNavigation = navigation;

    mPlayerModel = playerModel;
    mTsumoResultModel = tsumoResultModel;
    mRonResultModel = ronResultModel;

    mCalculator = new Calculator(mBoardModel, mPlayerModel);

    observableHand = new ReadOnlyRxProperty<>(mPlayerModel.formattedHand);
    observableCall1 = new ReadOnlyRxProperty<>(mPlayerModel.formattedCall1.doOnNext(this::showKanDialog));
    observableCall2 = new ReadOnlyRxProperty<>(mPlayerModel.formattedCall2.doOnNext(this::showKanDialog));
    observableCall3 = new ReadOnlyRxProperty<>(mPlayerModel.formattedCall3.doOnNext(this::showKanDialog));
    observableCall4 = new ReadOnlyRxProperty<>(mPlayerModel.formattedCall4.doOnNext(this::showKanDialog));

    observableHandHPos = new ReadOnlyRxProperty<>(mPlayerModel.formattedHandHPos);

    observableHandTextColor = new ReadOnlyRxProperty<>(mPlayerModel.formattedHandTextColor);
    observableCall1TextColor = new ReadOnlyRxProperty<>(mPlayerModel.formattedCall1TextColor);
    observableCall2TextColor = new ReadOnlyRxProperty<>(mPlayerModel.formattedCall2TextColor);
    observableCall3TextColor = new ReadOnlyRxProperty<>(mPlayerModel.formattedCall3TextColor);
    observableCall4TextColor = new ReadOnlyRxProperty<>(mPlayerModel.formattedCall4TextColor);

    observableSeatWind = new ReadOnlyRxProperty<>(mPlayerModel.formattedWind);
    observableRoundWind = new ReadOnlyRxProperty<>(mBoardModel.formattedWind);
    observableDora = new ReadOnlyRxProperty<>(mPlayerModel.formattedDora);
    observableStick100 = new ReadOnlyRxProperty<>(mBoardModel.formattedStick100);

    Disposable disposableHand = commandHand.subscribe(n -> {
      Log.v("DEBUG", "command hand");
      mPlayerModel.selectHand();
    });

    Disposable disposableCall1 = commandCall1.subscribe(n -> {
      Log.v("DEBUG", "command call 1");
      mPlayerModel.selectCall1();
    });

    Disposable disposableCall2 = commandCall2.subscribe(n -> {
      Log.v("DEBUG", "command call 2");
      mPlayerModel.selectCall2();
    });

    Disposable disposableCall3 = commandCall3.subscribe(n -> {
      Log.v("DEBUG", "command call 3");
      mPlayerModel.selectCall3();
    });

    Disposable disposableCall4 = commandCall4.subscribe(n -> {
      Log.v("DEBUG", "command call 4");
      mPlayerModel.selectCall4();
    });

    Disposable disposableSeatWind = commandSeatWind.subscribe(n -> {
      Log.v(TAG, "on click seat wild");
      mPlayerModel.nextWind();
      calculate();
    });

    Disposable disposableRoundWind = commandRoundWind.subscribe(n -> {
      Log.v(TAG, "on click round wild");
      mBoardModel.nextWind();
      calculate();
    });

    Disposable disposableDora = commandDora.subscribe(n -> {
      Log.v(TAG, "on click dora");
      mPlayerModel.increaseDora();
    });

    Disposable disposableStick100 = commandStick100.subscribe(n -> {
      Log.v(TAG, "on click stick100");
      mBoardModel.increaseStick100();
    });

    Disposable disposableRemove = commandRemove.subscribe(n -> {
      Log.v(TAG, "on click remove");
      try {
        mPlayerModel.removeTile();
        calculate();
      } catch (TileShortageException e) {
        Log.v(TAG, e.getMessage());
      }
    });

    Disposable disposable = command.subscribe(i -> {
      Log.v(TAG, "test command " + i);
    });

  }

  public boolean onLongClickBackSpace() {
    Log.v(TAG, "on click reset");
    mPlayerModel.initialize();
    mBoardModel.initialize();
    return true;
  }


  public void selectTile(final View view) {
    commandHand.execute(NoParameter.INSTANCE);
    mPlayerModel.selectTile(Integer.parseInt(view.getTag().toString()));
    calculate();
  }

  @BindingAdapter("srcCompat")
  public static void srcCompat(ImageView view, int resourceId) {
    view.setImageResource(resourceId);
  }

  @BindingAdapter("android:layout_marginBottom")
  public static void setLayoutMarginBottom(View view, int margin) {
    ViewGroup.LayoutParams params = view.getLayoutParams();
    if (params instanceof ViewGroup.MarginLayoutParams) {
      ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams) params;
      margins.bottomMargin = margin;
      view.requestLayout();
    }
  }

  public void setTile(final Tile tile) {
    Log.v(TAG, "set tile : " + tile.getName());
    // add to hand or call
    mPlayerModel.addTile(tile);

    // calculate
    calculate();
  }

  private void calculate() {
    try {
      long startTime = System.currentTimeMillis();
      mCalculator.calculate();
      mTsumoResultModel.applyResult(mCalculator.getTsumoResult());
      mRonResultModel.applyResult(mCalculator.getRonResult());
      mNavigation.updateResult();
      Log.v(TAG, "measure time : " + (System.currentTimeMillis() - startTime) + " msec");
    } catch (InvalidCallCaseException e) {
      Log.v(TAG, e.getMessage());
    }
  }

  private void showKanDialog(final List<Integer> list) {
    if (list.size() < 4) {
      return;
    }
    int preId = -1;
    for (int id : list) {
      if (id != preId && preId != -1) {
        return;
      }
      preId = id;
    }

    mNavigation.showKanDialog();
  }
}


