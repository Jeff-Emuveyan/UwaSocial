package com.bellogate_caliphate.uwasocial.features.posts.ui.state

sealed interface PostsUiState {
    object Loading : PostsUiState
    object Success : PostsUiState
    data class Error(val message: String) : PostsUiState
    object Empty : PostsUiState
    object Offline : PostsUiState
}
