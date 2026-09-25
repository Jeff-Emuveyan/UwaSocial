package com.bellogate_caliphate.uwasocial.features.posts.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bellogate_caliphate.uwasocial.core.network.NetworkObserver
import com.bellogate_caliphate.uwasocial.domain.model.FeedPost
import com.bellogate_caliphate.uwasocial.domain.usecase.getfeedposts.GetFeedPostsUseCase
import com.bellogate_caliphate.uwasocial.domain.usecase.togglelikepost.ToggleLikePostUseCase
import com.bellogate_caliphate.uwasocial.features.posts.ui.state.PostsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    getFeedPostsUseCase: GetFeedPostsUseCase,
    private val toggleLikePostUseCase: ToggleLikePostUseCase,
    private val networkObserver: NetworkObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

    private val _isOffline = MutableStateFlow(value = false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    val postsFlow: Flow<PagingData<FeedPost>> = getFeedPostsUseCase()
        .cachedIn(viewModelScope)

    init {
        observeNetworkState()
    }

    fun toggleLike(postId: Long) {
        viewModelScope.launch {
            toggleLikePostUseCase(postId = postId)
        }
    }

    fun setUiState(state: PostsUiState) {
        _uiState.value = state
    }

    private fun observeNetworkState() {
        viewModelScope.launch {
            networkObserver.observeNetworkConnectivity().collect { isConnected ->
                _isOffline.value = !isConnected
            }
        }
    }
}
