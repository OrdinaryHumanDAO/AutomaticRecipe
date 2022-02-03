package com.example.calendartestapp.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeRepository @Inject constructor(private val recipeDao: RecipeDao) {

    suspend fun insertRecipe(recipeEntity: RecipeEntity){
        recipeDao.insertRecipe(recipeEntity)
    }

    suspend fun updateRecipe(recipeEntity: RecipeEntity){
        recipeDao.updateRecipe(recipeEntity)
    }

    suspend fun allDelete(){
        recipeDao.allDelete()
    }

    fun loadAllRecipe(): Flow<List<RecipeEntity>> = recipeDao.loadAllRecipe()

    suspend fun loadTodayRecipe(today: Int): RecipeEntity = recipeDao.loadTodayRecipe(today)

    suspend fun loadDetailRecipe(id: Int): RecipeEntity = recipeDao.loadDetailRecipe(id)
}