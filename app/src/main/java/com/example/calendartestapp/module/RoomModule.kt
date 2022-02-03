package com.example.calendartestapp.module

import android.content.Context
import androidx.room.Room
import com.example.calendartestapp.database.AppDatabase
import com.example.calendartestapp.retrofit.RakutenService
import com.example.calendartestapp.retrofit.RecipeApiRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = AppDatabase.getInstance(context)
    //Room.databaseBuilder(context, AppDatabase::class.java, "recipe_entity").build()

    @Singleton
    @Provides
    fun provideDao(db: AppDatabase) = db.RecipeDao()


    @Singleton
    @Provides
    fun provideRecipeApi(): RakutenService {

        val RAKUTEN_URL = "https://app.rakuten.co.jp/services/api/Recipe/"

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(RAKUTEN_URL)
            .build()
            .create(RakutenService::class.java)

        return retrofit
    }

}