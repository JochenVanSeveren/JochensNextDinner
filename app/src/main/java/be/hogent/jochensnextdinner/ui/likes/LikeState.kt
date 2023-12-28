package be.hogent.jochensnextdinner.ui.likes

import Like

data class LikeListState(val likeList: List<Like> = listOf())

sealed interface LikeApiState {
    object Success : LikeApiState
    data class Error(val message: String) : LikeApiState
    object Loading : LikeApiState
}