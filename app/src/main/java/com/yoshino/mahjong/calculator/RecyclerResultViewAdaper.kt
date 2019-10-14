package com.yoshino.mahjong.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yoshino.mahjong.calculator.databinding.ItemFanBinding
import com.yoshino.mahjong.calculator.utils.FanCalculator
import com.yoshino.mahjong.calculator.viewmodels.SimpleResultViewModel

/*
class TodoRecyclerViewAdapter(lifeCycleOwner: LifecycleOwner, private val viewModel: SimpleResultViewModel):
        RecyclerView.Adapter<TodoRecyclerViewAdapter.BindingHolder>() {

    // Adapterが知っている一番最後のlist
    private var oldList: List<FanCalculator.Fan> = listOf()

    // 初期化時にviewModelを購読して、変更があった場合、RecyclerViewに通知する
    init {
        this.viewModel.observableFan.observe(lifeCycleOwner, Observer {
            this.updateList(it)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoRecyclerViewAdapter.BindingHolder {
        val binding = ItemFanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun getItemCount(): Int = this.viewModel.observableFan.size

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.binding.fan = this.oldList[position]
    }

    private fun updateList(newList: List<FanCalculator.Fan>) {
        val diffResult = DiffUtil.calculateDiff(TodoRecyclerViewAdapter.TodoDiffUtilImpl(this.oldList, newList))
        diffResult.dispatchUpdatesTo(this)
        this.oldList = newList
    }

    data class BindingHolder(val binding: ItemFanBinding) : RecyclerView.ViewHolder(binding.root)

    class TodoDiffUtilImpl(private val oldList: List<FanCalculator.Fan>, private val newList: List<FanCalculator.Fan>): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
                = oldList[oldItemPosition].registerDate == newList[newItemPosition].registerDate

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
    }
}

*/
