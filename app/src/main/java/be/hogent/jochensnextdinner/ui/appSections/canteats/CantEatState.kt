package be.hogent.jochensnextdinner.ui.appSections.canteats

import androidx.work.WorkInfo
import be.hogent.jochensnextdinner.model.CantEat

/**
 * Data class representing the state of the CantEat list.
 *
 * @property cantEatList The list of CantEat items.
 */
data class CantEatListState(val cantEatList: List<CantEat> = listOf())

/**
 * Sealed interface representing the state of the CantEat API.
 */
sealed interface CantEatApiState {
    /**
     * Object representing the success state of the CantEat API.
     */
    object Success : CantEatApiState

    /**
     * Data class representing the error state of the CantEat API.
     *
     * @property message The error message.
     */
    data class Error(val message: String) : CantEatApiState

    /**
     * Object representing the loading state of the CantEat API.
     */
    object Loading : CantEatApiState
}