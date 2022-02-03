package com.example.calendartestapp.alertDialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.example.calendartestapp.R
import com.example.calendartestapp.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteRecipeDialog: DialogFragment() {

    companion object {
        private const val REQUEST_POSITIVE_BUTTON_KEY = "RequestPositiveButtonKey"
        private const val REQUEST_NEGATIVE_BUTTON_KEY = "RequestNegativeButtonKey"
        private const val POSITIVE_BUTTON_TEXT_KEY = "PositiveButtonTextKey"
        private const val NEGATIVE_BUTTON_TEXT_KEY = "NegativeButtonTextKey"
    }

    class Builder(private val fragment: Fragment) {
        private val bundle = Bundle()

        fun setPositiveButton(buttonText: String, listener: (() -> Unit)? = null): Builder {
            fragment.childFragmentManager
                .setFragmentResultListener(
                    REQUEST_POSITIVE_BUTTON_KEY,
                    fragment.viewLifecycleOwner
                ) { _, _ ->
                    listener?.invoke()
                }
            return this.apply {
                bundle.putString(POSITIVE_BUTTON_TEXT_KEY, buttonText)
            }
        }

        fun setNegativeButton(buttonText: String, listener: (() -> Unit)? = null): Builder {
            fragment.childFragmentManager
                .setFragmentResultListener(
                    REQUEST_NEGATIVE_BUTTON_KEY,
                    fragment.viewLifecycleOwner
                ) { _, _ ->
                    listener?.invoke()
                }
            return this.apply {
                bundle.putString(NEGATIVE_BUTTON_TEXT_KEY, buttonText)
            }
        }

        fun build(): DeleteRecipeDialog {
            return DeleteRecipeDialog().apply {
                arguments = bundle
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val recipeDao = AppDatabase.getInstance(context!!).RecipeDao()

        // カスタムレイアウトを生成
        val dialogView = requireActivity()
            .layoutInflater
            .inflate(R.layout.delete_recipe_dialog, null)!!

        val builder = AlertDialog.Builder(requireContext())

        builder
            .setView(dialogView)
            .setTitle("注意!")
            .setPositiveButton("OK") { _,_  ->
                lifecycleScope.launch(Dispatchers.IO) {
                    recipeDao.allDelete()
                    Log.d("aiueo", "recipeEntityを削除しました")
                }
                dismiss()
                setFragmentResult(
                    REQUEST_POSITIVE_BUTTON_KEY,
                    bundleOf()
                )
            }
            .setNegativeButton("CANCEL") { dialog, id ->
                dismiss()
                setFragmentResult(
                    REQUEST_NEGATIVE_BUTTON_KEY,
                    bundleOf()
                )
            }

        return builder.create()
    }

}