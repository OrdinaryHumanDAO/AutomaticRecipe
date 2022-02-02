package com.example.calendartestapp.calendarRecipeToday

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.calendartestapp.R
import com.example.calendartestapp.calendarRecipe.CalendarRecipeViewModel
import com.example.calendartestapp.database.AppDatabase
import com.example.calendartestapp.database.RecipeEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class CalendarRecipeTodayFragment : Fragment() {

    //private lateinit var viewModel: CalendarRecipeTodayViewModel
    private val viewModel by viewModels<CalendarRecipeTodayViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar_recipe_today, container, false)

        //val viewModelFactory = CalendarRecipeTodayViewModelFactory(view.context)
        //viewModel = ViewModelProvider(this, viewModelFactory).get(CalendarRecipeTodayViewModel::class.java)

        val recipeTitle: TextView = view.findViewById(R.id.recipeTitle)
        val recipeImage: ImageView = view.findViewById(R.id.recipeImage)
        val recipeUrl: TextView = view.findViewById(R.id.recipeUrl)
        val recipeMaterial: TextView = view.findViewById(R.id.recipeMaterial)

        viewModel.loadTodayRecipe()
        viewModel.todayRecipe.observe(this, {
            if(it != null){
                recipeTitle.text = it.recipeTitle
                Glide.with(this).load(it.foodImageUrl).into(recipeImage)
                recipeUrl.text = it.recipeUrl
                recipeMaterial.text = it.recipeMaterial.toString()
            } else {
                if(findNavController().currentDestination?.id == R.id.calendarRecipeTodayFragment) {
                    findNavController().navigate(R.id.action_calendarRecipeTodayFragment_to_noDataFragment)
                }
                Log.d("aiueo", "noDataに遷移します(today)")
            }
        })

        return view
    }

}