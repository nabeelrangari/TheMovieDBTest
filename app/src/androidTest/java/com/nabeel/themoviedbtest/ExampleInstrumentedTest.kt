package com.nabeel.themoviedbtest

import android.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.nabeel.themoviedbtest.view.activity.HomeActivity
import com.nabeel.themoviedbtest.view.fragment.MovieFragment
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Pattern.matches


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.nabeel.themoviedbtest", appContext.packageName)

    }

    companion object {
        @BeforeClass
        fun setup() {
            // Setting up
        }
    }

    @Test
    fun testing() {
        // Testing...

    }

    @get:Rule
    val mActivityRule: ActivityTestRule<HomeActivity> = ActivityTestRule<HomeActivity>(
        HomeActivity::class.java
    )

    @Before
    fun yourSetUPFragment() {
        mActivityRule.activity
            .supportFragmentManager.beginTransaction()
    }



}
