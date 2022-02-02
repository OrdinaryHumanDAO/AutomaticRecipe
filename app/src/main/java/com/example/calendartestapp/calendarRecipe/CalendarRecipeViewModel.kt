package com.example.calendartestapp.calendarRecipe

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.calendartestapp.database.AppDatabase
import com.example.calendartestapp.database.RecipeEntity
import com.example.calendartestapp.database.RecipeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

class CalendarRecipeViewModel @ViewModelInject constructor(
    //@ApplicationContext context: Context
    private val repository: RecipeRepository
) : ViewModel() {

    //private val repository: RecipeRepository

    init {
//        val recipeDB = AppDatabase.getInstance(context).RecipeDao()
//        repository = RecipeRepository(recipeDB)

    }

    val recipeEntity: LiveData<List<RecipeEntity>> = repository.loadAllRecipe().asLiveData()

    fun allDelete() {
        viewModelScope.launch {
            repository.allDelete()
        }
    }

}

//Hiltを使うといらないけど一応残しとく
//class CalendarRecipeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CalendarRecipeViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return CalendarRecipeViewModel(context) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//
//}
