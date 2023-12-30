package be.hogent.jochensnextdinner

import be.hogent.jochensnextdinner.data.CachingCantEatRepository
import be.hogent.jochensnextdinner.data.CantEatRepository
import be.hogent.jochensnextdinner.data.database.CantEatDao
import be.hogent.jochensnextdinner.fake.FakeCantEatRepository
import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.network.CantEatApiService
import be.hogent.jochensnextdinner.network.postCantEatAsFlow
import be.hogent.jochensnextdinner.ui.appSections.canteats.CantEatApiState
import be.hogent.jochensnextdinner.ui.appSections.canteats.CantEatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CantEatViewModelTest {

    private val someCantEatName = "some cant eat name"

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @Test
    fun savingCantEatUpdatesUiListState() = runTest {
        // Arrange
//        val cantEatRepository = mock(CantEatRepository::class.java)
        val viewModel = CantEatViewModel(FakeCantEatRepository())

        val cantEat = CantEat(name = someCantEatName)
        val cantEatList = listOf(cantEat)

        // Use a non-null matcher
//        whenever(cantEatRepository.saveCantEat(argThat { name == someCantEatName })).thenReturn(cantEat)
//        whenever(cantEatRepository.getCantEats()).thenReturn(flowOf(cantEatList))

        // Act
        viewModel.saveCantEat(cantEat)
        advanceUntilIdle() // Ensure all coroutines have completed

        // Assert
        val savedCantEat = viewModel.uiListState.value.cantEatList.firstOrNull()
        // TODO BUG THIS LIST STAYS EMPTY CHECK DEBUGGING
        Assert.assertNotNull(savedCantEat)
        Assert.assertEquals(cantEat.name, savedCantEat?.name)
        Assert.assertEquals(viewModel.cantEatApiState, CantEatApiState.Success)
    }
}




class TestDispatcherRule @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
