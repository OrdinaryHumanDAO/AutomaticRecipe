package com.example.calendartestapp.retrofit

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Runnable
import javax.inject.Inject

class RecipeApiRepository @Inject constructor(
    private val userService: RakutenService
) {


    suspend fun getCategoryIdApi(categoryId: MutableList<Int>): MutableList<Int> {
        var list = mutableListOf<Int>()
        userService.getRecipeCategory("large", "categoryId").enqueue(object :
            Callback<RecipeCategoryId> {
            override fun onResponse(
                call: Call<RecipeCategoryId>,
                response: Response<RecipeCategoryId>
            ) {
                for (i in response.body()!!.result.large) {
                    categoryId.add(i.categoryId)
                }
                categoryId.shuffle()
                list = categoryId
            }

            override fun onFailure(call: Call<RecipeCategoryId>, t: Throwable) {
            }
        })
        delay(200)
        return list
    }

    suspend fun getApiFromMoshi(categoryId: String, recipeInfo: MutableList<Recipe.Result>):
    MutableList<Recipe.Result> {
        var list = mutableListOf<Recipe.Result>()

        userService.getRecipeInfo(categoryId).enqueue(object: Callback<Recipe> {
            override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {

                for(i in response.body()!!.result){
                    recipeInfo.add(i)
                }
                list = recipeInfo
                //resipeInfo.value?.add("あいうえお") //これだと出来ない
            }
            override fun onFailure(call: Call<Recipe>, t: Throwable) {
            }
        })
        delay(200)
        return list
    }

}