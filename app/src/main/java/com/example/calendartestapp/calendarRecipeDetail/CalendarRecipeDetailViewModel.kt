package com.example.calendartestapp.calendarRecipeDetail

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.calendartestapp.database.AppDatabase
import com.example.calendartestapp.database.RecipeEntity
import com.example.calendartestapp.database.RecipeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

class CalendarRecipeDetailViewModel @ViewModelInject constructor(
    private val repository: RecipeRepository
): ViewModel() {

    val detailRecipe = MutableLiveData<RecipeEntity>()

    val recipeEntity: LiveData<List<RecipeEntity>> = repository.loadAllRecipe().asLiveData()

    fun loadDetailRecipe(id: Int){
        viewModelScope.launch {
            detailRecipe.value = repository.loadDetailRecipe(id)
        }
    }

}
