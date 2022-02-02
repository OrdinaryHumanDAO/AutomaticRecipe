package com.example.calendartestapp.calendar

import com.example.calendartestapp.retrofit.Recipe

sealed class CalendarItem {
    data class Header(val text: String) : CalendarItem()
    data class Day(
        val day: String,
        val recipeInfo: Recipe.Result?,
        val isToDay: Boolean = false,
        ) : CalendarItem()
}