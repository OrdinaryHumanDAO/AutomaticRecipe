package com.example.calendartestapp.retrofit

import android.os.Parcelable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import kotlinx.android.parcel.Parcelize as Parcelize

interface RakutenService {
    @GET("CategoryRanking/20170426?applicationId=1004689038418697304")
    fun getRecipeInfo(
        @Query("categoryId") categoryId: String,
    )
            : Call<Recipe>

    @GET("CategoryList/20170426?applicationId=1004689038418697304")
    fun getRecipeCategory(
        @Query("categoryType") categoryType: String,
        @Query("element") element: String,
    )
            : Call<RecipeCategoryId>
}

data class Recipe(
    val result: List<Result>
){
    @Parcelize
    data class Result(
        val foodImageUrl: String,
        val recipeCost: String,
        val recipeIndication: String,
        val recipeTitle: String,
        val recipeUrl: String,
        val recipeMaterial: List<String>
    ) : Parcelable
}
data class RecipeCategoryId(
    val result: Result,
) {
    data class Result(
        val large: List<Large>,
    ){
        data class Large(
            val categoryId: Int,
        )
    }
}




