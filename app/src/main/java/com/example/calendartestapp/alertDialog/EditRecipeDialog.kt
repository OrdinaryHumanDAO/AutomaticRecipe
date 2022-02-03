package com.example.calendartestapp.alertDialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope

import com.example.calendartestapp.R
import com.example.calendartestapp.database.AppDatabase
import com.example.calendartestapp.database.RecipeEntity
import com.example.calendartestapp.retrofit.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditRecipeDialog : DialogFragment() {

    private var data: Int = 0
    private var recipeTitle: String = ""
    private var recipe: String = ""
    private var foodImageUrl: String = ""
    private var recipeIndication: String = ""
    private var recipeCost: String = ""
    private var recipeMaterial: String = ""


    companion object {
        private const val DATE_KEY = "DateKey"
        private const val RT_KEY = "RecipeTitleKey"
        private const val R_KEY = "RecipeKey"
        private const val FIU_KEY = "FoodImageUrl"
        private const val RI_KEY = "RecipeIndicationKey"
        private const val RC_KEY = "RecipeCostKey"
        private const val RM_KEY = "RecipeMaterialKey"

        private const val REQUEST_POSITIVE_BUTTON_KEY = "RequestPositiveButtonKey"
        private const val REQUEST_NEGATIVE_BUTTON_KEY = "RequestNegativeButtonKey"
        private const val POSITIVE_BUTTON_TEXT_KEY = "PositiveButtonTextKey"
        private const val NEGATIVE_BUTTON_TEXT_KEY = "NegativeButtonTextKey"
    }

    class Builder(private val fragment: Fragment) {
        private val bundle = Bundle()

        fun setDate(date: Int): Builder {
            return this.apply {
                bundle.putInt(DATE_KEY, date)
            }
        }

        fun setRecipeTitle(recipeTitle: String): Builder {
            return this.apply {
                bundle.putString(RT_KEY, recipeTitle)
            }
        }

        fun setRecipe(recipe: String): Builder {
            return this.apply {
                bundle.putString(R_KEY, recipe)
            }
        }

        fun setFootImageUrl(foodImageUrl: String): Builder {
            return this.apply {
                bundle.putString(FIU_KEY, foodImageUrl)
            }
        }

        fun setRecipeIndication(recipeIndication: String): Builder {
            return this.apply {
                bundle.putString(RI_KEY, recipeIndication)
            }
        }

        fun setRecipeCost(recipeCost: String): Builder {
            return this.apply {
                bundle.putString(RC_KEY, recipeCost)
            }
        }

        fun setRecipeMaterial(recipeMaterial: ArrayList<String>): Builder {
            return this.apply {
                bundle.putStringArrayList(RM_KEY, recipeMaterial)
            }
        }

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

        fun build(): EditRecipeDialog {
            return EditRecipeDialog().apply {
                arguments = bundle
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let {
            data = it.getInt(DATE_KEY, 0)
            recipeTitle = it.getString(RT_KEY, "")
            recipe = it.getString(R_KEY, "")
            foodImageUrl = it.getString(FIU_KEY, "")
            recipeIndication = it.getString(RI_KEY, "")
            recipeCost = it.getString(RC_KEY, "")
            recipeMaterial = it.getStringArrayList(RM_KEY).toString().drop(1).dropLast(1)
        }

        // カスタムレイアウトを生成
        val dialogView = requireActivity()
            .layoutInflater
            .inflate(R.layout.edit_recipe_dialog, null)!!

        val rt:EditText = dialogView.findViewById(R.id.recipeTitle)
        val r:EditText = dialogView.findViewById(R.id.recipe)
        val fiu:EditText = dialogView.findViewById(R.id.foodImageUrl)
        val ri:EditText = dialogView.findViewById(R.id.recipeIndication)
        val rc:EditText = dialogView.findViewById(R.id.recipeCost)
        val rm:EditText = dialogView.findViewById(R.id.recipeMaterial)

        rt.setText(recipeTitle, TextView.BufferType.NORMAL)
        r.setText(recipe, TextView.BufferType.NORMAL)
        fiu.setText(foodImageUrl, TextView.BufferType.NORMAL)
        ri.setText(recipeIndication, TextView.BufferType.NORMAL)
        rc.setText(recipeCost, TextView.BufferType.NORMAL)
        //dropでリストの[]を取り除いてる
        rm.setText(recipeMaterial, TextView.BufferType.NORMAL)


        val builder = AlertDialog.Builder(requireContext())
        builder
            .setView(dialogView)
            .setTitle("編集")
            .setPositiveButton("OK") { dialog, id ->
                val recipeDao = AppDatabase.getInstance(context!!).RecipeDao()

                val recipeEntity = RecipeEntity(
                    data,
                    fiu.text.toString(),
                    rc.text.toString(),
                    ri.text.toString(),
                    rt.text.toString(),
                    r.text.toString(),
                    rm.text.split(",")
                )

                Toast.makeText(context, rt.text, Toast.LENGTH_LONG).show()

                lifecycleScope.launch(Dispatchers.IO) {
                    recipeDao.updateRecipe(recipeEntity)
                    Log.d("aiueo","recipeEntityを編集しました")
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