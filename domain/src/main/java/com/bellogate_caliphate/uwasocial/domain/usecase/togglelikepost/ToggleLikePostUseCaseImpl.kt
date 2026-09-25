package com.bellogate_caliphate.uwasocial.domain.usecase.togglelikepost

import com.bellogate_caliphate.uwasocial.domain.repository.PostRepository
import javax.inject.Inject

internal class ToggleLikePostUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository
) : ToggleLikePostUseCase {
    override suspend operator fun invoke(postId: Long) {
        postRepository.toggleLike(postId)
    }
}
