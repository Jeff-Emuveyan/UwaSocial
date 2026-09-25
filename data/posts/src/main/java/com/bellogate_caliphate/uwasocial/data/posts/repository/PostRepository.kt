package com.bellogate_caliphate.uwasocial.data.posts.repository

import androidx.paging.PagingData
import com.bellogate_caliphate.uwasocial.domain.model.FeedPost
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getFeedPosts(): Flow<PagingData<FeedPost>>
    suspend fun toggleLike(postId: Long)
}
