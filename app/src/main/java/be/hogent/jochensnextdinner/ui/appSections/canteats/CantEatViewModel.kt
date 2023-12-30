package be.hogent.jochensnextdinner.ui.appSections.canteats

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import be.hogent.jochensnextdinner.JndApplication
import be.hogent.jochensnextdinner.data.CantEatRepository
import be.hogent.jochensnextdinner.model.CantEat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

class CantEatViewModel(
    private val cantEatRepository: CantEatRepository
) : ViewModel() {
    lateinit var uiListState: StateFlow<CantEatListState>

    var cantEatApiState: CantEatApiState by mutableStateOf(CantEatApiState.Loading)
        private set

    init {
        getCantEatsFromRepo()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                cantEatApiState = CantEatApiState.Loading
                cantEatRepository.refresh()
                cantEatApiState = CantEatApiState.Success
            } catch (e: Exception) {
                cantEatApiState = CantEatApiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }


    fun saveCantEat(cantEat: CantEat) {
        try {
            cantEatApiState = CantEatApiState.Loading
            val errorMessage = validateInput(cantEat)
            if (errorMessage != null) {
                cantEatApiState = CantEatApiState.Error(errorMessage)
                return
            }
            viewModelScope.launch() {
                cantEatRepository.saveCantEat(cantEat)
                getCantEatsFromRepo()
            }
            cantEatApiState = CantEatApiState.Success
        } catch (e: IOException) {
            cantEatApiState = CantEatApiState.Error(e.message ?: "Unknown error")
        }
    }

    fun deleteCantEat(cantEat: CantEat) {
        try {
            cantEatApiState = CantEatApiState.Loading
            viewModelScope.launch {
                cantEatRepository.deleteCantEat(cantEat)
            }
            cantEatApiState = CantEatApiState.Success
        } catch (e: IOException) {
            cantEatApiState = CantEatApiState.Error(e.message ?: "Unknown error")
        }
    }

    private fun validateInput(cantEat: CantEat): String? {
        return if (cantEat.name.isEmpty()) {
            "Name cannot be empty"
        } else {
            null
        }
    }

    private fun getCantEatsFromRepo() {
        try {
            cantEatApiState = CantEatApiState.Loading
            uiListState = cantEatRepository.getCantEats().map { CantEatListState(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000L),
                    initialValue = CantEatListState(),
                )
            cantEatApiState = CantEatApiState.Success
        } catch (e: IOException) {
            cantEatApiState = CantEatApiState.Error(e.message ?: "Unknown error")
        }
    }

    companion object {
        private var Instance: CantEatViewModel? = null
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                if (Instance == null) {
                    val application = (this[APPLICATION_KEY] as JndApplication)
                    val cantEatRepository = application.appContainer.cantEatRepository
                    Instance = CantEatViewModel(cantEatRepository = cantEatRepository)
                }
                Instance!!
            }
        }
    }
}