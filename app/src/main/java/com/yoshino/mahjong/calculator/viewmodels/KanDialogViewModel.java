package com.yoshino.mahjong.calculator.viewmodels;

import com.yoshino.mahjong.calculator.models.PlayerModel;

import jp.keita.kagurazaka.rxproperty.ReadOnlyRxProperty;

public class KanDialogViewModel {

    public final ReadOnlyRxProperty<Integer> observableImageId;

    public KanDialogViewModel(final PlayerModel playerModel) {
        observableImageId = new ReadOnlyRxProperty<>(playerModel.formattedKanTile);
    }
}
