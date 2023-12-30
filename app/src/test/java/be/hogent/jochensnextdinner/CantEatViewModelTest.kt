package be.hogent.jochensnextdinner

import be.hogent.jochensnextdinner.data.CantEatRepository
import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.ui.appSections.canteats.CantEatApiState
import be.hogent.jochensnextdinner.ui.appSections.canteats.CantEatViewModel
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
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
class CantEatViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val cantEatRepository = mockk<CantEatRepository>()
    private lateinit var viewModel: CantEatViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { cantEatRepository.getCantEats() } returns flowOf(listOf(CantEat(name = "Test")))
        viewModel = CantEatViewModel(cantEatRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `loadCantEats() should call repository getCantEats`() = runTest {
        viewModel.refresh()
        advanceUntilIdle()

        coVerify { cantEatRepository.getCantEats() }
    }

    @Test
    fun `loadCantEats() should return error state on repository failure`() = runTest {
        val errorMessage = "Error fetching cant eats"
        coEvery { cantEatRepository.refresh() } throws Exception(errorMessage)

        viewModel.refresh()
        advanceUntilIdle()

        val state = viewModel.cantEatApiState
        TestCase.assertTrue(state is CantEatApiState.Error && state.message == errorMessage)
    }

    @Test
    fun `saveCantEat() should call repository saveCantEat`() = runTest {
        val cantEat = CantEat(name = "Test")
        coEvery { cantEatRepository.saveCantEat(any()) } returns cantEat

        viewModel.saveCantEat(cantEat)
        advanceUntilIdle()

        coVerify { cantEatRepository.saveCantEat(cantEat) }
    }

    @Test
    fun `deleteCantEat() should call repository deleteCantEat`() = runTest {
        val cantEat = CantEat(name = "Test")
        coEvery { cantEatRepository.deleteCantEat(any()) } just Runs

        viewModel.deleteCantEat(cantEat)
        advanceUntilIdle()

        coVerify { cantEatRepository.deleteCantEat(cantEat) }
    }

    @Test
    fun `refresh() should call repository refresh`() = runTest {
        coEvery { cantEatRepository.refresh() } just Runs

        viewModel.refresh()
        advanceUntilIdle()

        coVerify { cantEatRepository.refresh() }
    }
}