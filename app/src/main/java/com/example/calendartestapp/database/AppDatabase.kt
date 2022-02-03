package com.example.calendartestapp.database

import android.content.Context
import androidx.room.*


@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(StringListTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun RecipeDao(): RecipeDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "recipe_entity")
                        .allowMainThreadQueries()
                        .build()
                }
                return instance!!
            }
        }
    }
}