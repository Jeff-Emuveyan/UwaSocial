package com.bellogate_caliphate.uwasocial.domain.usecase.getfeedposts

import androidx.paging.PagingData
import com.bellogate_caliphate.uwasocial.domain.model.FeedPost
import com.bellogate_caliphate.uwasocial.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetFeedPostsUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository
) : GetFeedPostsUseCase {
    override operator fun invoke(): Flow<PagingData<FeedPost>> {
        return postRepository.getFeedPosts()
    }
}
