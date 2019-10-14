package com.yoshino.mahjong.calculator;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.yoshino.mahjong.calculator.databinding.ItemFanBinding;
import com.yoshino.mahjong.calculator.models.FanModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class RecyclerFanViewAdapter extends RecyclerView.Adapter<RecyclerFanViewAdapter.ItemViewHolder> {
  private ArrayList<FanModel> mFanList;

  public static class ItemViewHolder extends RecyclerView.ViewHolder {
    private ItemFanBinding mBinding;

    ItemViewHolder(final ItemFanBinding binding) {
      super(binding.getRoot());
      mBinding = binding;
    }

    public ItemFanBinding getBinding() {
      return mBinding;
    }
  }

  public RecyclerFanViewAdapter(final ArrayList<FanModel> fanList) {
    mFanList = fanList;
  }

  @NotNull
  @Override
  public ItemViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
    ItemFanBinding binding = ItemFanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new ItemViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NotNull ItemViewHolder holder, int position) {
    FanModel fan = mFanList.get(position);
    holder.getBinding().setFan(fan);
  }

  @Override
  public int getItemCount() {
    return (mFanList == null) ? 0 : mFanList.size();
  }
}
