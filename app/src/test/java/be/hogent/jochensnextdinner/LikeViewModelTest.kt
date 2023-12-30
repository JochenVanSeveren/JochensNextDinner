package be.hogent.jochensnextdinner

import Like
import be.hogent.jochensnextdinner.data.LikeRepository
import be.hogent.jochensnextdinner.ui.appSections.likes.LikeViewModel
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
class LikeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val likeRepository = mockk<LikeRepository>()
    private lateinit var viewModel: LikeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { likeRepository.getLikes() } returns flowOf(listOf(Like(name = "Test", category = "test")))
        viewModel = LikeViewModel(likeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `loadLikes() should call repository getLikes`() = runTest {
        viewModel.refresh()
        advanceUntilIdle()

        coVerify { likeRepository.getLikes() }
    }

}