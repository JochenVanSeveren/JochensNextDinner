package be.hogent.jochensnextdinner

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import be.hogent.jochensnextdinner.ui.JochensNextDinnerApp
import be.hogent.jochensnextdinner.utils.JndNavigationType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /*Test nav host not working*/
    private lateinit var navController: NavHostController

    @Before
    fun setup() {
        composeTestRule.setContent {
            navController = rememberNavController()
            JochensNextDinnerApp(
                navController,
                navigationType = JndNavigationType.BOTTOM_NAVIGATION
            )
        }
    }

    @Test
    fun navigateToCantEatScreen() {
        composeTestRule.onNodeWithTag(getResourceString(R.string.cant_eat_screen)+ "NavIcon")
            .performClick()
        composeTestRule.onNodeWithText(getResourceString(R.string.cant_eat_screen))
            .assertIsDisplayed()
    }

    @Test
    fun navigateToLikeScreen() {
        composeTestRule.onNodeWithTag(getResourceString(R.string.like_screen)+ "NavIcon")
            .performClick()
        composeTestRule.onNodeWithText(getResourceString(R.string.like_screen))
            .assertIsDisplayed()
    }

    @Test
    fun navigateToRecipeScreen() {
        composeTestRule.onNodeWithTag(getResourceString(R.string.recipe_screen)+ "NavIcon")
            .performClick()
        composeTestRule.onNodeWithText(getResourceString(R.string.recipe_screen))
            .assertIsDisplayed()
    }

    @Test
    fun navigateBackToStartScreen() {
        composeTestRule.onNodeWithTag(getResourceString(R.string.cant_eat_screen)+ "NavIcon")
            .performClick()
        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.navigate_up))
            .performClick()
        composeTestRule.onNodeWithText(getResourceString(R.string.app_name))
            .assertIsDisplayed()
    }

    private fun getResourceString(key: Int): String {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        return context.resources.getString(key)
    }
}

