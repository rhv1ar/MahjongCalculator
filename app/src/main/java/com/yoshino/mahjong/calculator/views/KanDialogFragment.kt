package com.yoshino.mahjong.calculator.views

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.yoshino.mahjong.calculator.R
import com.yoshino.mahjong.calculator.databinding.FragmentKanDialogBinding
import com.yoshino.mahjong.calculator.models.PlayerModel
import com.yoshino.mahjong.calculator.viewmodels.KanDialogViewModel

class KanDialogFragment() : DialogFragment() {
  private var mKanDialogViewModel: KanDialogViewModel? = null

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val playerModel = arguments?.get("playermodel") as PlayerModel?
    mKanDialogViewModel = KanDialogViewModel(playerModel)
    //val builder = AlertDialog.Builder(activity)
    val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
    val binding = DataBindingUtil.inflate<FragmentKanDialogBinding>(LayoutInflater.from(activity), R.layout.fragment_kan_dialog, null, false)
    binding.viewModel = mKanDialogViewModel
    //val view = binding.root
    builder.setView(binding.root)
            .setPositiveButton("OK") { _, _ ->
              playerModel?.reverseKan()
            }
    return builder.create()
  }
}
