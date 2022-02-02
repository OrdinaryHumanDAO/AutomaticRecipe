package com.example.calendartestapp.calendarRecipeDetail

import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.calendartestapp.R
import com.example.calendartestapp.alertDialog.EditRecipeDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class CalendarRecipeDetailFragment : Fragment() {

    private val args: CalendarRecipeDetailFragmentArgs by navArgs()
    //private lateinit var viewModel: CalendarRecipeDetailViewModel
    private val viewModel by viewModels<CalendarRecipeDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar_recipe_detail, container, false)

        val recipeTitle: TextView = view.findViewById(R.id.recipeTitle)
        val recipeImage: ImageView = view.findViewById(R.id.recipeImage)
        val recipeUrl: TextView = view.findViewById(R.id.recipeUrl)
        val recipeMaterial: TextView = view.findViewById(R.id.recipeMaterial)

        //val viewModelFactory = CalendarRecipeDetailViewModelFactory(view.context)
        //viewModel = ViewModelProvider(this,viewModelFactory).get(CalendarRecipeDetailViewModel::class.java)


        viewModel.recipeEntity.observe(this,{
            viewModel.loadDetailRecipe(args.id)
        })

        viewModel.detailRecipe.observe(this,{
            recipeTitle.text = it.recipeTitle
            Glide.with(this).load(it.foodImageUrl).into(recipeImage)
            recipeUrl.text = it.recipeUrl
            recipeMaterial.text = it.recipeMaterial.toString()
        })



        val btnRecipeEdit: ImageButton = view.findViewById(R.id.btnRecipeEdit)
        btnRecipeEdit.setOnClickListener {
            Log.d("aiueo", "editDialogを表示します")
            val dialog = EditRecipeDialog.Builder(this)
            dialog
                .setDate(viewModel.detailRecipe.value!!.date)
                .setRecipeTitle(viewModel.detailRecipe.value!!.recipeTitle)
                .setRecipe(viewModel.detailRecipe.value!!.recipeUrl)
                .setFootImageUrl(viewModel.detailRecipe.value!!.foodImageUrl)
                .setRecipeIndication(viewModel.detailRecipe.value!!.recipeIndication)
                .setRecipeCost(viewModel.detailRecipe.value!!.recipeCost)
                .setRecipeMaterial(viewModel.detailRecipe.value!!.recipeMaterial as ArrayList<String>)
                .build()
                .show(childFragmentManager,"editRecipeDialog")
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.setting).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

}
