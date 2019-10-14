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

import com.yoshino.mahjong.calculator.R;
import com.yoshino.mahjong.calculator.databinding.FragmentSimpleRonResultBinding;
import com.yoshino.mahjong.calculator.models.PlayerModel;
import com.yoshino.mahjong.calculator.models.ResultModel;
import com.yoshino.mahjong.calculator.viewmodels.SimpleResultViewModel;

public class SimpleRonResultFragment extends Fragment implements SimpleResultNavigation {
    /**
     * Class tag.
     */
    private static final String TAG = SimpleRonResultFragment.class.getSimpleName();

    private static final String ARG_RESULT = "arg_result";
    private static final String ARG_PLAYER = "arg_player";

    private SimpleResultViewModel mSimpleResultViewModel;

    private ResultModel mResultModel;
    private PlayerModel mPlayerModel;

    public static SimpleRonResultFragment newInstance(final ResultModel resultModel, final PlayerModel playerModel) {
        Log.v(TAG, "newInstance");
        SimpleRonResultFragment fragment = new SimpleRonResultFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESULT, resultModel);
        args.putSerializable(ARG_PLAYER, playerModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.v(TAG, "onCreateView");

        mResultModel = (ResultModel) getArguments().getSerializable(ARG_RESULT);
        mPlayerModel = (PlayerModel) getArguments().getSerializable(ARG_PLAYER);
        mSimpleResultViewModel = new SimpleResultViewModel(this, mResultModel, mPlayerModel);

        // Initialize DataBinding
        FragmentSimpleRonResultBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_simple_ron_result, container, false);
        binding.setViewModel(mSimpleResultViewModel);

        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_simple_ron_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void startDetailResultActivity() {
        Intent intent = new Intent(getActivity(), DetailResultActivity.class);
        startActivity(intent);
    }

}
