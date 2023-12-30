package be.hogent.jochensnextdinner.ui.appSections.likes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import be.hogent.jochensnextdinner.JndApplication
import be.hogent.jochensnextdinner.data.LikeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing Like data.
 *
 * @property likeRepository The repository for managing Like data.
 */
class LikeViewModel(private val likeRepository: LikeRepository) : ViewModel() {

    // State for the list of Like items
    lateinit var uiListState: StateFlow<LikeListState>

    // State for the API status
    var likeApiState: LikeApiState by mutableStateOf(LikeApiState.Loading)
        private set

    init {
        getLikesFromRepo()
    }

    /**
     * Refreshes the list of Like items from the repository.
     */
    fun refresh() {
        viewModelScope.launch {
            try {
                likeApiState = LikeApiState.Loading
                likeRepository.refresh()
                likeApiState = LikeApiState.Success
            } catch (e: Exception) {
                likeApiState = LikeApiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Fetches the list of Like items from the repository.
     */
    private fun getLikesFromRepo() {
        try {
            likeApiState = LikeApiState.Loading
            uiListState = likeRepository.getLikes().map { LikeListState(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000L),
                    initialValue = LikeListState(),
                )
            likeApiState = LikeApiState.Success
        } catch (e: Exception) {
            likeApiState = LikeApiState.Error(e.message ?: "Unknown error")
        }
    }

    companion object {
        private var Instance: LikeViewModel? = null
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                if (Instance == null) {
                    val application =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as JndApplication)
                    val likeRepository = application.appContainer.likeRepository
                    Instance = LikeViewModel(likeRepository = likeRepository)
                }
                Instance!!
            }
        }
    }
}