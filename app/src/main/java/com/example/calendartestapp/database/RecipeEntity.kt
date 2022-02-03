package com.example.calendartestapp.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.calendartestapp.retrofit.Recipe
import com.example.calendartestapp.retrofit.Recipe.Result
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "recipe_entity")
data class RecipeEntity(
    @PrimaryKey
    val date: Int,
    val foodImageUrl: String,
    val recipeCost: String,
    val recipeIndication: String,
    val recipeTitle: String,
    val recipeUrl: String,
    val recipeMaterial: List<String>
): Parcelable