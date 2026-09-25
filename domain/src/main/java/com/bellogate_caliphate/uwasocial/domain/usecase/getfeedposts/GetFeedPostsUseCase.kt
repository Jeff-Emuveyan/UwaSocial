package com.bellogate_caliphate.uwasocial.domain.usecase.getfeedposts

import androidx.paging.PagingData
import com.bellogate_caliphate.uwasocial.domain.model.FeedPost
import kotlinx.coroutines.flow.Flow

interface GetFeedPostsUseCase {
    operator fun invoke(): Flow<PagingData<FeedPost>>
}
