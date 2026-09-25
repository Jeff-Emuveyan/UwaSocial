package com.bellogate_caliphate.uwasocial.domain.usecase.togglelikepost

interface ToggleLikePostUseCase {
    suspend operator fun invoke(postId: Long)
}
