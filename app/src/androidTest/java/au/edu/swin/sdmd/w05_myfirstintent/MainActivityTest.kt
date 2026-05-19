package au.edu.swin.sdmd.w05_myfirstintent

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests fot Challenge Task 2B
 * Tests cover:
 * - Editing a spot name and verifying it updates to the main screen
 * - Validation errors showing when required fields are cleared
 * - Confirming back press is blocked validation fails
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Test 1: Edit spot name and verify it appears updated on main screen
     */
    @Test
    fun editSpotName_appearsOnMainScreen() {
        val newName = "Updated Iceberg"

        // Click spot 1's image to open detail screen
        onView(withId(R.id.imgSpot1)).perform(click())

        // Clear the name field and type a new name
        onView(withId(R.id.editName))
            .perform(clearText(), typeText(newName), closeSoftKeyboard())

        // Press back to save and return to main screen
        pressBack()

        // Verify the updated name now appears on the main screen card
        onView(withId(R.id.tvName1)).check(matches(withText(newName)))
    }

    /**
     * Test 2: Clearing name field and pressing back should show the name validation error
     * and keep the user on the detail screen
     */
    @Test
    fun shortReview_showsValidationError() {
        // Open spot 1
        onView(withId(R.id.imgSpot1)).perform(click())
        // Replace review with something too short
        onView(withId(R.id.editReview))
            .perform(scrollTo(), clearText(), typeText("Too Short"), closeSoftKeyboard())
        // Attempt to go back
        pressBack()
        // Review error should appear
        onView(withId(R.id.errorReview)).check(matches(isDisplayed()))
    }

    /**
     * Test 4: Clearing the location field and pressing back should show location error
     */
    @Test
    fun emptyLocation_showsValidationError() {
        // Open spot 2
        onView(withId(R.id.imgSpot2)).perform(click())
        // Clear Location field
        onView(withId(R.id.editLocation)).perform(clearText(), closeSoftKeyboard())
        // Attempt to go back
        pressBack()
        // Location error should appear
        onView(withId(R.id.errorLocation)).check(matches(isDisplayed()))
    }
}
