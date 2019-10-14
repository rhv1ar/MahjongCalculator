package com.yoshino.mahjong.calculator.viewmodels;

import android.util.Log;

import com.yoshino.mahjong.calculator.models.PlayerModel;
import com.yoshino.mahjong.calculator.models.ResultModel;
import com.yoshino.mahjong.calculator.views.SimpleResultNavigation;

import io.reactivex.disposables.Disposable;
import jp.keita.kagurazaka.rxproperty.NoParameter;
import jp.keita.kagurazaka.rxproperty.ReadOnlyRxProperty;
import jp.keita.kagurazaka.rxproperty.RxCommand;

public class SimpleResultViewModel {
    private static final String TAG = SimpleResultViewModel.class.getSimpleName();

    private final ResultModel mResultModel;
    private final PlayerModel mPlayerModel;

    public final ReadOnlyRxProperty<String> observableFan;
    public final ReadOnlyRxProperty<String> observableFu;
    public final ReadOnlyRxProperty<String> observableTotalPoint;
    public final ReadOnlyRxProperty<String> observableChildPoint;
    public final ReadOnlyRxProperty<String> observableParentPoint;

    public final ReadOnlyRxProperty<Integer> observableReach;
    public final ReadOnlyRxProperty<Integer> observableDoubleReach;
    public final ReadOnlyRxProperty<Integer> observableFirstGoAround;
    public final ReadOnlyRxProperty<Integer> observableFinalTileWin;
    public final ReadOnlyRxProperty<Integer> observableRinsyan;

    public final RxCommand<NoParameter> commandDetailResult = new RxCommand<>();

    public final RxCommand<NoParameter> onClickReach = new RxCommand<>();
    public final RxCommand<NoParameter> onClickDoubleReach = new RxCommand<>();
    public final RxCommand<NoParameter> onClickFirstGoAround = new RxCommand<>();
    public final RxCommand<NoParameter> onClickRinsyan = new RxCommand<>();
    public final RxCommand<NoParameter> onClickFinalTileWin = new RxCommand<>();

    public SimpleResultViewModel(final SimpleResultNavigation navigation, final ResultModel resultModel, final PlayerModel playerModel) {
        mResultModel = resultModel;
        mPlayerModel = playerModel;

        observableFan = new ReadOnlyRxProperty<>(mResultModel.formattedFan);
        observableFu = new ReadOnlyRxProperty<>(mResultModel.formattedFu);
        observableTotalPoint = new ReadOnlyRxProperty<>(mResultModel.formattedTotalPoint);
        observableChildPoint = new ReadOnlyRxProperty<>(mResultModel.formattedChildPoint);
        observableParentPoint = new ReadOnlyRxProperty<>(mResultModel.formattedParentPoint);


        observableReach = new ReadOnlyRxProperty<>(mPlayerModel.formattedReachBgColor);
        observableDoubleReach = new ReadOnlyRxProperty<>(mPlayerModel.formattedDoubleReachBgColor);
        observableFirstGoAround = new ReadOnlyRxProperty<>(mPlayerModel.formattedFirstGoAround);
        observableFinalTileWin = new ReadOnlyRxProperty<>(mPlayerModel.formattedFinalTileWinBgColor);
        observableRinsyan = new ReadOnlyRxProperty<>(mPlayerModel.formattedRinsyanBgColor);

        Disposable disposableDetail = commandDetailResult.subscribe(n -> {
            //navigation.startDetailResultActivity();
        });

        Disposable disposableReach = onClickReach.subscribe(n -> {
            Log.v(TAG, "on click reach");
            mPlayerModel.switchReach();
        });
        Disposable disposableDoubleReach = onClickDoubleReach.subscribe(n -> {
            Log.v(TAG, "on click double reach");
            mPlayerModel.switchDoubleReach();
        });
        Disposable disposableFirstGoAround = onClickFirstGoAround.subscribe(n -> {
            Log.v(TAG, "on click first go around");
            mPlayerModel.switchFirstGoAround();
        });
        Disposable disposableFinalTileWin = onClickFinalTileWin.subscribe(n -> {
            Log.v(TAG, "on click final tile win");
            mPlayerModel.switchFinalTileWin();
        });
        Disposable disposableRinsyan = onClickRinsyan.subscribe(n -> {
            Log.v(TAG, "on click rinsyan");
            mPlayerModel.switchRinsyan();
        });

    }
}
