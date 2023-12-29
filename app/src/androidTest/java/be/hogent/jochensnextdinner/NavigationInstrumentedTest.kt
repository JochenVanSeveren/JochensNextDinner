package be.hogent.jochensnextdinner

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import be.hogent.jochensnextdinner.model.Recipe
import be.hogent.jochensnextdinner.ui.JochensNextDinnerApp
import be.hogent.jochensnextdinner.ui.recipes.RecipeViewModel
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
            JochensNextDinnerApp(navController)
        }
    }

    @Test
    fun navigateToCantEatScreen() {
        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.navigate_to_cant_eat_screen))
            .performClick()
        composeTestRule.onNodeWithText(getResourceString(R.string.cant_eat_screen))
            .assertIsDisplayed()
    }

    @Test
    fun navigateToLikeScreen() {
        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.navigate_to_like_screen))
            .performClick()
        composeTestRule.onNodeWithText(getResourceString(R.string.like_screen))
            .assertIsDisplayed()
    }

    @Test
    fun navigateToRecipeScreen() {
        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.navigate_to_recipe_screen))
            .performClick()
        composeTestRule.onNodeWithText(getResourceString(R.string.recipe_screen))
            .assertIsDisplayed()
    }

    @Test
    fun navigateBackToStartScreen() {
        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.navigate_to_cant_eat_screen))
            .performClick()
        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.navigate_up))
            .performClick()
        composeTestRule.onNodeWithText(getResourceString(R.string.app_name))
            .assertIsDisplayed()
    }


    @Test
    fun navigateToRecipeDetailScreen() {
        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.navigate_to_recipe_screen))
            .performClick()

        composeTestRule.waitForIdle()
        // TODO: BUG no recipes found


        composeTestRule.onNodeWithText("Pad thai à la Jochen 4p")
            .performClick()

        composeTestRule.onNodeWithText("Pad thai à la Jochen 4p")
            .assertIsDisplayed()
    }

    @Test
    fun navigateBackFromRecipeDetailScreen() {
        // TODO: BUG no recipes found
        navigateToRecipeDetailScreen()

        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.navigate_up))
            .performClick()

        composeTestRule.onNodeWithText(getResourceString(R.string.recipe_screen))
            .assertIsDisplayed()
    }

    private fun getResourceString(key: Int): String {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        return context.resources.getString(key)
    }
}