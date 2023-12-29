package be.hogent.jochensnextdinner

import be.hogent.jochensnextdinner.fake.FakeCantEatRepository
import be.hogent.jochensnextdinner.model.CantEat
import be.hogent.jochensnextdinner.ui.appSections.canteats.CantEatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class CantEatViewModelTest {

    private val someCantEatName = "some cant eat name"

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun savingCantEatChangesState() {
        // Arrange
        val viewModel = CantEatViewModel(
            cantEatRepository = FakeCantEatRepository(),
        )
        val newCantEat = CantEat(name = someCantEatName)

        // Act
        viewModel.saveCantEat(newCantEat)
        val savedCantEat = viewModel.uiListState.value.cantEatList.firstOrNull()

        // Assert
        Assert.assertNotNull(savedCantEat)
        Assert.assertEquals(savedCantEat?.name, someCantEatName)
        Assert.assertEquals(1, viewModel.uiListState.value.cantEatList.size)
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
