package be.hogent.jochensnextdinner

import CantEatScreen
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import be.hogent.jochensnextdinner.fake.FakeCantEatRepository
import be.hogent.jochensnextdinner.ui.appSections.canteats.CantEatViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CantEatScreenUserInteractionTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeRepository: FakeCantEatRepository
    private lateinit var viewModel: CantEatViewModel

    @Before
    fun setup() {
        fakeRepository = FakeCantEatRepository()
        viewModel = CantEatViewModel(fakeRepository)

        composeTestRule.setContent {
            CantEatScreen(viewModel)
        }
    }

    @Test
    fun testAddNewItem() {
        // Click the FAB to add a new item
        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.add_button))
            .performClick()

        // A new item with an empty name should appear
        composeTestRule.onNodeWithText("").assertExists()

        // Fill in the name and save the item
        composeTestRule.onNodeWithText("").performTextInput("New Item")
        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.save))
            .performClick()

        // The new item should now exist
        composeTestRule.onNodeWithText("New Item").assertExists()

    }

    @Test
    fun testErrorOnSave() {
        // Click the FAB to add a new item
        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.add_button))
            .performClick()

        // A new item with an empty name should appear
        composeTestRule.onNodeWithText("").assertExists()

        // Try to save the item without filling in the name
        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.save))
            .performClick()

        // An error message should be displayed
        composeTestRule.onNodeWithText("Name cannot be empty").assertExists()
    }

    @Test
    fun testDeleteItem() {
        // Ensure the item exists
        composeTestRule.onNodeWithText("Test Item 2").assertExists()

        // Locate and click the edit button for "Test Item 1"
        composeTestRule.onNodeWithTag("EditButton-Test Item 2").performClick()

        composeTestRule.onNodeWithContentDescription(getResourceString(R.string.delete))
            .performClick()

        // The item should no longer exist
        composeTestRule.onNodeWithText("Test Item 2").assertDoesNotExist()
    }

    private fun getResourceString(key: Int): String {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        return context.resources.getString(key)
    }
}