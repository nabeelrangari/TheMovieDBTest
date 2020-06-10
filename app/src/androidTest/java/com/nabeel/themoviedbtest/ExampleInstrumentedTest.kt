package com.nabeel.themoviedbtest

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.nabeel.themoviedbtest.view.activity.HomeActivity
import com.nabeel.themoviedbtest.view.fragment.MovieFragment
import org.hamcrest.Matchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


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

    /* @Test
     public fun testCasesForRecyclerViewClick(){
         Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
             .inRoot(RootMatchers.withDecorView(Matchers.`is`(mActivityRule.activity.window.decorView)))
             .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,click()))
     }*/

}
