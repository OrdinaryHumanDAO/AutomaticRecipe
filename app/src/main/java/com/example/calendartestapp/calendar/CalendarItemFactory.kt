package com.example.calendartestapp.calendar

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.asLiveData
import com.example.calendartestapp.database.AppDatabase
import com.example.calendartestapp.database.RecipeEntity
import com.example.calendartestapp.database.RecipeRepository
import com.example.calendartestapp.retrofit.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CalendarItemFactory {

    companion object {

        private const val MAX_ROW = 6
        private const val NUM_WEEK = 7

        fun create(offsetMonth: Int, recipeInfo: List<Recipe.Result>, context: Context): Array<CalendarItem> {

            val scope = CoroutineScope(Dispatchers.IO)

            val recipeDao = AppDatabase.getInstance(context).RecipeDao()
            val repository = RecipeRepository(recipeDao)

            val itemList: MutableList<CalendarItem> = arrayListOf()
            val calendar = Calendar.getInstance()
            val currentDay = calendar.get(Calendar.DATE)
            calendar.add(Calendar.MONTH, offsetMonth)
            val dayOfMonth = calendar.getActualMaximum(Calendar.DATE) // 当月は何日か？
            calendar.set(Calendar.DATE, 1)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // SUNDAY(1)、MONDAY(2) ...

            // ヘッダー部分
            arrayOf("日", "月", "火", "水", "木", "金", "土").forEach {
                itemList.add(CalendarItem.Header(it))
            }
            val headerSize = itemList.size


            // 開始日までを埋める処理
            for (i in 1 until dayOfWeek) {
                itemList.add(CalendarItem.Day("", null))
            }

            val sdf = SimpleDateFormat("yyyyMMdd")
            // 有効日を埋める処理
            for (i in 1..dayOfMonth) {

                val recipe = repository.loadAllRecipe().asLiveData()
                if(recipe.value?.size != dayOfMonth){
                    calendar.set(Calendar.DATE, i)
                    val date = sdf.format(calendar.time).toInt()
                    val recipeEntity = RecipeEntity(
                        date,
                        recipeInfo[i-1].foodImageUrl,
                        recipeInfo[i-1].recipeCost,
                        recipeInfo[i-1].recipeIndication,
                        recipeInfo[i-1].recipeTitle,
                        recipeInfo[i-1].recipeUrl,
                        recipeInfo[i-1].recipeMaterial,
                    )

                    scope.launch {
                        repository.insertRecipe(recipeEntity)
                    }
                } else {
                    Log.d("aiueo", "${recipe.value?.size}")
                }

                if (offsetMonth == 0 && i == currentDay) {
                    itemList.add(CalendarItem.Day("$i", recipeInfo[i-1], true))
                } else {
                    itemList.add(CalendarItem.Day("$i", recipeInfo[i-1]))
                }
            }
            // 余りセルを埋める処理
            val fillSize = (MAX_ROW * NUM_WEEK + headerSize) - itemList.size
            for (i in 0 until fillSize) {
                itemList.add(CalendarItem.Day("", null))
            }
            return itemList.toTypedArray()
        }

    }

}