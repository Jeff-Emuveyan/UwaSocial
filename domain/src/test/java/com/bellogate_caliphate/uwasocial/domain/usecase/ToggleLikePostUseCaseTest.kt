package com.bellogate_caliphate.uwasocial.domain.usecase

import com.bellogate_caliphate.uwasocial.domain.repository.PostRepository
import com.bellogate_caliphate.uwasocial.domain.usecase.togglelikepost.ToggleLikePostUseCaseImpl
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleLikePostUseCaseTest {

    private val repository: PostRepository = mockk(relaxed = true)
    private val useCase = ToggleLikePostUseCaseImpl(repository)

    @Test
    fun invoke_delegatesToggleLikeToRepository() = runTest {
        val postId = 42L

        useCase(postId)

        coVerify(exactly = 1) { repository.toggleLike(postId) }
    }
}
