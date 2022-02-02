package com.example.calendartestapp.calendarRecipeToday

import android.os.Build
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.calendartestapp.R
import com.example.calendartestapp.database.AppDatabase
import com.example.calendartestapp.database.RecipeRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import javax.inject.Inject


@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk =[28],
    application = HiltTestApplication::class
)
class CalendarRecipeTodayFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun fragmentTest() {
        launchFragmentInHiltContainer<CalendarRecipeTodayFragment> {
            assert(
                this.view?.findViewById<TextView>(
                    R.id.make
                )?.text.toString() == "作り方"
            )
        }
    }

}