package be.hogent.jochensnextdinner

import be.hogent.jochensnextdinner.data.RecipeRepository
import be.hogent.jochensnextdinner.model.Recipe
import be.hogent.jochensnextdinner.ui.appSections.recipes.detail.RecipeDetailViewModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase
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
class RecipeDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val recipeRepository = mockk<RecipeRepository>()
    private lateinit var viewModel: RecipeDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { recipeRepository.getRecipe(any()) } returns flowOf( Recipe(slug = "first", title = "title1", ingredients = listOf("ingredient1"), optionalIngredients = listOf("optionalIngredient1"), herbs = listOf("herb1"), steps = listOf("step1"), image = "image1"))
        viewModel = RecipeDetailViewModel(recipeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `loadRecipe() should call repository getRecipe`() = runTest {
        viewModel.getRecipeDetail(recipeId = 0)
        advanceUntilIdle()

        coVerify { recipeRepository.getRecipe(0) }
    }

    @Test
    fun `loadRecipe() should return success state with data on successful fetch`() = runTest {
        val mockRecipe = Recipe(slug = "first", title = "title1", ingredients = listOf("ingredient1"), optionalIngredients = listOf("optionalIngredient1"), herbs = listOf("herb1"), steps = listOf("step1"), image = "image1")
        coEvery { recipeRepository.getRecipe(0) } returns flowOf(mockRecipe)

        viewModel.getRecipeDetail(recipeId = 0)
        advanceUntilIdle()

        val state = viewModel.recipe.value
        TestCase.assertTrue(state == mockRecipe)
    }
}