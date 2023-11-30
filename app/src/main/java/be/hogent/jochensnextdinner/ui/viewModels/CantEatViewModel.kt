package be.hogent.jochensnextdinner.ui.viewModels

import android.content.ContentValues.TAG
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
    object Error : CantEatUiState
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
        try {
            val cantEats = jochensNextDinnerRepository.getCantEats()
            cantEatUiState = CantEatUiState.Success(cantEats)
        } catch (e: IOException) {
            Log.e("CantEatViewModel", "IOException: ${e.message}")
            cantEatUiState = CantEatUiState.Error
        } catch (e: HttpException) {
            Log.e("CantEatViewModel", "HttpException: ${e.message}")
            cantEatUiState = CantEatUiState.Error
        }
    }
}

    companion object {
        private const val TAG = "CantEatViewModel"

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