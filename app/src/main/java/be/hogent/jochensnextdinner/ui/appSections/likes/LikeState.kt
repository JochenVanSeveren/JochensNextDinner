package be.hogent.jochensnextdinner.ui.appSections.likes

import Like

/**
 * Data class representing the state of the Like list.
 *
 * @property likeList The list of Like items.
 */
data class LikeListState(val likeList: List<Like> = listOf())

/**
 * Sealed interface representing the state of the Like API.
 */
sealed interface LikeApiState {
    /**
     * Object representing the success state of the Like API.
     */
    object Success : LikeApiState

    /**
     * Data class representing the error state of the Like API.
     *
     * @property message The error message.
     */
    data class Error(val message: String) : LikeApiState

    /**
     * Object representing the loading state of the Like API.
     */
    object Loading : LikeApiState
}