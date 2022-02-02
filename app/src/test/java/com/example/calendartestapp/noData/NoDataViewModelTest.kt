package com.example.calendartestapp.noData

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendartestapp.database.AppDatabase
import com.example.calendartestapp.database.RecipeRepository
import com.example.calendartestapp.retrofit.RakutenService
import com.example.calendartestapp.retrofit.RecipeApiRepository
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class NoDataViewModelTest : TestCase() {
    // LiveDataをテストするために必要
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val RAKUTEN_URL = "https://app.rakuten.co.jp/services/api/Recipe/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(RAKUTEN_URL)
        .build()

    private val behavior = NetworkBehavior.create()
    private val delegate = MockRetrofit.Builder(retrofit).networkBehavior(behavior).build()
        .create(RakutenService::class.java)

    private val searchService = MockUserService(delegate)
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val recipeDao = AppDatabase.getInstance(context).RecipeDao()
    private val viewModel =
        NoDataViewModel(RecipeRepository(recipeDao), RecipeApiRepository(searchService))

    @Before
    public override fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    public override fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun 初めてのテスト(){
        behavior.apply {
            setDelay(0, TimeUnit.MILLISECONDS) // 即座に結果が返ってくるようにする
            setVariancePercent(0)
            setFailurePercent(0)
            setErrorPercent(0)
        }

        viewModel.categoryIdApi()
        val result = LiveDataTestUtil.getValue(viewModel.categoryId)

        // データが存在するか
        assertThat(result).isEmpty()

    }
}