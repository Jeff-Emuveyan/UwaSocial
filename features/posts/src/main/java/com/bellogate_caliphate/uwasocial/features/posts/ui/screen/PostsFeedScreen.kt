package com.bellogate_caliphate.uwasocial.features.posts.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bellogate_caliphate.uwasocial.domain.model.FeedPost
import com.bellogate_caliphate.uwasocial.features.posts.ui.components.EmptyStateComponent
import com.bellogate_caliphate.uwasocial.features.posts.ui.components.ErrorStateComponent
import com.bellogate_caliphate.uwasocial.features.posts.ui.components.OfflineBanner
import com.bellogate_caliphate.uwasocial.features.posts.ui.components.OfflineStateComponent
import com.bellogate_caliphate.uwasocial.features.posts.ui.components.PostCard
import com.bellogate_caliphate.uwasocial.features.posts.ui.components.ShimmerFeed
import com.bellogate_caliphate.uwasocial.features.posts.ui.viewmodel.PostsViewModel

@Composable
fun PostsFeedScreen(
    modifier: Modifier = Modifier,
    viewModel: PostsViewModel = hiltViewModel()
) {
    val pagingItems = viewModel.postsFlow.collectAsLazyPagingItems()
    val isOffline by viewModel.isOffline.collectAsState()

    PostsFeedScreenContent(
        pagingItems = pagingItems,
        isOffline = isOffline,
        onLikeClick = { viewModel.toggleLike(it) },
        modifier = modifier
    )
}

@Composable
fun PostsFeedScreenContent(
    pagingItems: LazyPagingItems<FeedPost>,
    isOffline: Boolean,
    onLikeClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val refreshState = pagingItems.loadState.refresh

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F17))
    ) {
        if (isOffline) {
            OfflineBanner()
        }

        when {
            refreshState is LoadState.Loading -> {
                ShimmerFeed(modifier = Modifier.padding(top = 12.dp))
            }
            refreshState is LoadState.Error -> {
                if (isOffline && pagingItems.itemCount == 0) {
                    OfflineStateComponent(onViewCachedFeedClick = { pagingItems.retry() })
                } else if (pagingItems.itemCount == 0) {
                    ErrorStateComponent(onRetryClick = { pagingItems.retry() })
                } else {
                    FeedList(
                        pagingItems = pagingItems,
                        onLikeClick = onLikeClick
                    )
                }
            }
            pagingItems.itemCount == 0 && refreshState is LoadState.NotLoading -> {
                EmptyStateComponent(onFindPeopleClick = { pagingItems.refresh() })
            }
            else -> {
                FeedList(
                    pagingItems = pagingItems,
                    onLikeClick = onLikeClick
                )
            }
        }
    }
}

@Composable
private fun FeedList(
    pagingItems: LazyPagingItems<FeedPost>,
    onLikeClick: (Long) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(count = pagingItems.itemCount) { index ->
            val post = pagingItems[index]
            if (post != null) {
                PostCard(post = post, onLikeClick = onLikeClick)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        if (pagingItems.loadState.append is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF3B82F6))
                }
            }
        }
    }
}
