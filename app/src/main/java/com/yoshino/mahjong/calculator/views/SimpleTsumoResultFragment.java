package com.yoshino.mahjong.calculator.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.yoshino.mahjong.calculator.R;
import com.yoshino.mahjong.calculator.RecyclerFanViewAdapter;
import com.yoshino.mahjong.calculator.databinding.FragmentSimpleTsumoResultBinding;
import com.yoshino.mahjong.calculator.models.PlayerModel;
import com.yoshino.mahjong.calculator.models.ResultModel;
import com.yoshino.mahjong.calculator.models.FanModel;
import com.yoshino.mahjong.calculator.viewmodels.SimpleResultViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SimpleTsumoResultFragment extends Fragment implements SimpleResultNavigation {
  /**
   * Class tag.
   */
  private static final String TAG = SimpleTsumoResultFragment.class.getSimpleName();

  private static final String ARG_TSUMO = "arg_tsumo";
  private static final String ARG_RESULT = "arg_result";
  private static final String ARG_PLAYER = "arg_player";

  FragmentSimpleTsumoResultBinding mBinding;

  private SimpleResultViewModel mSimpleResultViewModel;

  private ResultModel mResultModel;
  private PlayerModel mPlayerModel;

  public static SimpleTsumoResultFragment newInstance(final ResultModel resultModel, final PlayerModel playerModel) {
    Log.v(TAG, "newInstance");
    SimpleTsumoResultFragment fragment = new SimpleTsumoResultFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_RESULT, resultModel);
    args.putSerializable(ARG_PLAYER, playerModel);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    Log.v(TAG, "onCreateView");

    mResultModel = (ResultModel) getArguments().getSerializable(ARG_RESULT);
    mPlayerModel = (PlayerModel) getArguments().getSerializable(ARG_PLAYER);
    mSimpleResultViewModel = new SimpleResultViewModel(this, mResultModel, mPlayerModel);

    // Initialize DataBinding
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_simple_tsumo_result, container, false);
    mBinding.setViewModel(mSimpleResultViewModel);

    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
    layoutManager.setFlexDirection(FlexDirection.ROW);
    layoutManager.setJustifyContent(JustifyContent.FLEX_START);


    mBinding.recyclerView.setLayoutManager(layoutManager);

    return mBinding.getRoot();
  }

  public void update() {
    ArrayList<FanModel> items = new ArrayList<>();
    items.add(new FanModel(1, "11111111"));
    items.add(new FanModel(1, "2222222"));
    items.add(new FanModel(1, "3333"));
    items.add(new FanModel(1, "444"));
    items.add(new FanModel(1, "555555"));
    items.add(new FanModel(1, "666666"));
    items.add(new FanModel(1, "7777777"));
    items.add(new FanModel(1, "888888888888888"));
    items.add(new FanModel(1, "9999999999999999999999999999999"));
    items.add(new FanModel(1, "0000000000000000"));
    //mBinding.recyclerView.setAdapter(new RecyclerFanViewAdapter(items));
    mBinding.recyclerView.setAdapter(new RecyclerFanViewAdapter(mResultModel.getFans()));
  }

  @Override
  public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
    Log.v(TAG, "onViewCreated");
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void startDetailResultActivity() {
    Intent intent = new Intent(getActivity(), DetailResultActivity.class);
    startActivity(intent);
  }

}
