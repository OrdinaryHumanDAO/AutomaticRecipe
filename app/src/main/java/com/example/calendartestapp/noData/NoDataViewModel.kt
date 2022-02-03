package com.example.calendartestapp.noData

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.calendartestapp.database.AppDatabase
import com.example.calendartestapp.database.RecipeEntity
import com.example.calendartestapp.database.RecipeRepository
import com.example.calendartestapp.retrofit.Recipe
import com.example.calendartestapp.retrofit.RecipeApiRepository
import com.example.calendartestapp.retrofit.RecipeCategoryId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoDataViewModel @ViewModelInject constructor(
    private val repository: RecipeRepository,
    private val recipeApiRepository: RecipeApiRepository
): ViewModel() {

    val recipeInfo = MutableLiveData<MutableList<Recipe.Result>>()

    val categoryId = MutableLiveData<MutableList<Int>>()

    var recipeEntity: LiveData<List<RecipeEntity>> = repository.loadAllRecipe().asLiveData()


    init {
        recipeInfo.value = mutableListOf()
        categoryId.value = mutableListOf()
    }

    fun categoryIdApi() {
        viewModelScope.launch {
            categoryId.value = recipeApiRepository.getCategoryIdApi(categoryId.value!!)
        }
    }

    fun recipeInfoApi(categoryId: Int){
        viewModelScope.launch {
            recipeInfo.value =
                recipeApiRepository.getApiFromMoshi(
                    categoryId.toString(),
                    recipeInfo.value!!
                )
        }
    }

    fun multipleRecipeInfoApi(categoryId: MutableList<Int>) {
        val hnd0 = Handler(Looper.getMainLooper())
        var cnt = 0
        val rnb0 = object : Runnable{
            override fun run() {
                recipeInfoApi(categoryId[cnt])
                if(cnt < 7){
                    hnd0.postDelayed(this,3000)
                }  else return
                cnt++
            }
        }
        hnd0.post(rnb0)
    }

//    fun getCategoryIdApi() {
//
//        val retrofit = ResipeApiRepository().getResipeApi()
//
//        retrofit.getRecipeCategory("large", "categoryId").enqueue(object:
//            Callback<RecipeCategoryId> {
//            override fun onResponse(call: Call<RecipeCategoryId>, response: Response<RecipeCategoryId>) {
//
//                val list = categoryId.value!!
//                for(i in response.body()!!.result.large) {
//                    list.add(i.categoryId)
//                }
//                list.shuffle()
//                categoryId.value = list
//                multipleApi(categoryId.value!!)
//            }
//            override fun onFailure(call: Call<RecipeCategoryId>, t: Throwable) {
//            }
//        })
//    }
//
//    fun multipleApi(categoryId: MutableList<Int>) {
//        val hnd0 = Handler(Looper.getMainLooper())
//        var cnt = 0
//        val rnb0 = object : Runnable{
//            override fun run() {
//                getApiFromMoshi(categoryId[cnt].toString())
//                if(cnt < 7){
//                    hnd0.postDelayed(this,4000)
//                }  else return
//                cnt++
//            }
//        }
//        hnd0.post(rnb0)
//    }
//
//    fun getApiFromMoshi(categoryId: String) {
//
//        val retrofit = ResipeApiRepository().getResipeApi()
//
//        retrofit.getRecipeInfo(categoryId).enqueue(object: Callback<Recipe> {
//            override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
//
//                val list = recipeInfo.value!!
//                for(i in response.body()!!.result){
//                    list.add(i)
//                }
//                recipeInfo.value = list
//                //resipeInfo.value?.add("あいうえお") //これだと出来ない
//            }
//            override fun onFailure(call: Call<Recipe>, t: Throwable) {
//            }
//        })
//    }
}