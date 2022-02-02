package com.example.calendartestapp.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)

    @Update
    suspend fun updateRecipe(recipeEntity: RecipeEntity)

    @Query("DELETE FROM recipe_entity")
    suspend fun allDelete()

    @Query("SELECT * FROM recipe_entity")
    fun loadAllRecipe(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipe_entity WHERE DATE == :today")
    suspend fun loadTodayRecipe(today: Int): RecipeEntity

    @Query("SELECT * FROM recipe_entity WHERE DATE == :id")
    suspend fun loadDetailRecipe(id: Int): RecipeEntity

}
