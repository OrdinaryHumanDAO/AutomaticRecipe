package com.example.calendartestapp.calendarRecipeToday

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.calendartestapp.database.AppDatabase
import com.example.calendartestapp.database.RecipeEntity
import com.example.calendartestapp.database.RecipeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarRecipeTodayViewModel @ViewModelInject constructor(
    private val repository: RecipeRepository
): ViewModel() {

    val todayRecipe = MutableLiveData<RecipeEntity>()


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadTodayRecipe(){
        viewModelScope.launch {
            todayRecipe.value = repository.loadTodayRecipe(getTodayDate())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTodayDate(): Int {
        val current = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val formatted = current.format(formatter)
        return formatted.toInt()
    }

}