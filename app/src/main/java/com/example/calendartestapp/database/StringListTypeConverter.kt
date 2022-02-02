package com.example.calendartestapp.database

import android.util.JsonReader
import android.util.JsonWriter
import androidx.room.TypeConverter
import java.io.IOException
import java.io.StringReader
import java.io.StringWriter

class StringListTypeConverter {
    @TypeConverter
    fun fromString(stringListString: String): List<String> {
        return stringListString.split(",").map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>): String {
        return stringList.joinToString(separator = ",")
    }
}
