package com.example.calendartestapp.noData

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.calendartestapp.R
import com.example.calendartestapp.calendar.CalendarItemFactory
import com.example.calendartestapp.calendarRecipeToday.CalendarRecipeTodayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_calendar_recipe.*
import android.graphics.drawable.Drawable

import android.graphics.drawable.LayerDrawable

import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.calendartestapp.BuildConfig
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NoDataFragment : Fragment() {

    //private lateinit var viewModel: NoDataViewModel
    private val viewModel by viewModels<NoDataViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_no_data, container, false)

        //val viewModelFactory = NoDataViewModelFactory(view.context)
        //viewModel = ViewModelProvider(this, viewModelFactory).get(NoDataViewModel::class.java)

        val btnSetRecipe: Button = view.findViewById(R.id.btnSetRecipe)
        btnSetRecipe.setOnClickListener {
            viewModel.categoryIdApi()
        }

        viewModel.categoryId.observe(this, {
            if(it.isNotEmpty()){
                viewModel.multipleRecipeInfoApi(viewModel.categoryId.value!!)
            }
        })

        viewModel.recipeEntity.observe(this,{
            if(it.isNotEmpty() && it.size == 31){
                if(findNavController().currentDestination?.id == R.id.noDataFragment) {
                    findNavController().popBackStack()
                    Log.d("aiueo", "popBackします")
                }
            }
        })


        viewModel.recipeInfo.observe(this,{
            if(it.isNotEmpty()) {
                if(it.map{ it1 -> it1.recipeTitle }.size == 32) {
                    CalendarItemFactory.create(0, it, view.context)
                    btnSetRecipe.isEnabled = false
                }
            }
        })

        return view
    }

}