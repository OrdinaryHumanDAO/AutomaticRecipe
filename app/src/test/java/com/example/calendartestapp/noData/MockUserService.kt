package com.example.calendartestapp.noData

import com.example.calendartestapp.retrofit.RakutenService
import com.example.calendartestapp.retrofit.Recipe
import com.example.calendartestapp.retrofit.RecipeCategoryId
import retrofit2.Call
import retrofit2.mock.BehaviorDelegate

class MockUserService(
    private val delegate: BehaviorDelegate<RakutenService>
): RakutenService {
    var response: List<Recipe>? = null

    override fun getRecipeInfo(categoryId: String): Call<Recipe> {
        return delegate.returningResponse(response).getRecipeInfo(categoryId)
    }

    override fun getRecipeCategory(categoryType: String, element: String): Call<RecipeCategoryId> {
        return delegate.returningResponse(response).getRecipeCategory(categoryType, element)
    }
}