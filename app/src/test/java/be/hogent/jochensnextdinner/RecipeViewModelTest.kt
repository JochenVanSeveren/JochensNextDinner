package be.hogent.jochensnextdinner

import be.hogent.jochensnextdinner.data.RecipeRepository
import be.hogent.jochensnextdinner.model.Recipe
import be.hogent.jochensnextdinner.ui.appSections.recipes.RecipeViewModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RecipeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val recipeRepository = mockk<RecipeRepository>()
    private lateinit var viewModel: RecipeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { recipeRepository.getRecipes() } returns flowOf(listOf( Recipe(slug = "first", title = "title1", ingredients = listOf("ingredient1"), optionalIngredients = listOf("optionalIngredient1"), herbs = listOf("herb1"), steps = listOf("step1"), image = "image1")))
        viewModel = RecipeViewModel(recipeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `loadRecipes() should call repository getRecipes`() = runTest {
        viewModel.refresh()
        advanceUntilIdle()

        coVerify { recipeRepository.getRecipes() }
    }
}