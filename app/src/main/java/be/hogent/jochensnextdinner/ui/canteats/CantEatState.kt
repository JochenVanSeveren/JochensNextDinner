package be.hogent.jochensnextdinner.ui.canteats

import androidx.work.WorkInfo
import be.hogent.jochensnextdinner.model.CantEat

data class CantEatListState(val cantEatList: List<CantEat> = listOf())

data class WorkerState(val workerInfo: WorkInfo? = null)

sealed interface CantEatApiState {
    object Success : CantEatApiState
    data class Error(val message: String) : CantEatApiState
    object Loading : CantEatApiState
}

