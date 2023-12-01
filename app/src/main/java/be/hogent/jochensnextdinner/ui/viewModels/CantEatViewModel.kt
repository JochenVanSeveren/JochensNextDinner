package be.hogent.jochensnextdinner.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import be.hogent.jochensnextdinner.JochensNextDinnerApplication
import be.hogent.jochensnextdinner.data.JochensNextDinnerRepository
import be.hogent.jochensnextdinner.model.CantEat
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface CantEatUiState {
    data class Success(val cantEats: List<CantEat>) : CantEatUiState
    data class Error(val message: String) : CantEatUiState
    object Loading : CantEatUiState
}

class CantEatViewModel(private val jochensNextDinnerRepository: JochensNextDinnerRepository) :
    ViewModel() {
    var cantEatUiState: CantEatUiState by mutableStateOf(CantEatUiState.Loading)
        private set

    init {
        getCantEats()
    }


fun getCantEats() {
    viewModelScope.launch {
        cantEatUiState = CantEatUiState.Loading
        cantEatUiState = try {
            val cantEats = jochensNextDinnerRepository.getCantEats()
            val sortedCantEats = cantEats.sortedBy { it.createdAt }
            CantEatUiState.Success(sortedCantEats)
        } catch (e: IOException) {
            Log.e("CantEatViewModel", "IOException: ${e.message}")
            CantEatUiState.Error("Failed to load CantEats: ${e.message}")
        } catch (e: HttpException) {
            Log.e("CantEatViewModel", "HttpException: ${e.message}")
            CantEatUiState.Error("Failed to load CantEats: ${e.message}")
        }
    }
}

    fun addCantEat(cantEat: CantEat) {
        viewModelScope.launch {
            try {
                jochensNextDinnerRepository.addCantEat(cantEat)
                getCantEats()
            } catch (e: Exception) {
                Log.e("CantEatViewModel", "Exception: ${e.message}")
                cantEatUiState = CantEatUiState.Error("Failed to add CantEat: ${e.message}")
            }
        }
    }

    fun saveCantEat(cantEat: CantEat) {
        viewModelScope.launch {
            try {
                jochensNextDinnerRepository.updateCantEat(cantEat)
                getCantEats()
            } catch (e: Exception) {
                Log.e("CantEatViewModel", "Exception: ${e.message}")
                cantEatUiState = CantEatUiState.Error("Failed to save CantEat: ${e.message}")
            }
        }
    }

    fun deleteCantEat(cantEat: CantEat) {
        viewModelScope.launch {
            try {
                jochensNextDinnerRepository.deleteCantEat(cantEat)
                getCantEats()
            } catch (e: Exception) {
                Log.e("CantEatViewModel", "Exception: ${e.message}")
                cantEatUiState = CantEatUiState.Error("Failed to delete CantEat: ${e.message}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as JochensNextDinnerApplication)
                val jochensNextDinnerRepository =
                    application.appContainer.jochensNextDinnerRepository
                CantEatViewModel(jochensNextDinnerRepository = jochensNextDinnerRepository)
            }
        }
    }
}