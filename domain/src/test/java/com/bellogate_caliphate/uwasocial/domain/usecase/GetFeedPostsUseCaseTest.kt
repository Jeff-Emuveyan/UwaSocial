package com.bellogate_caliphate.uwasocial.domain.usecase

import androidx.paging.PagingData
import com.bellogate_caliphate.uwasocial.domain.repository.PostRepository
import com.bellogate_caliphate.uwasocial.domain.usecase.getfeedposts.GetFeedPostsUseCaseImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Test

class GetFeedPostsUseCaseTest {

    private val repository: PostRepository = mockk()
    private val useCase = GetFeedPostsUseCaseImpl(repository)

    @Test
    fun invoke_returnsPagingDataFlowFromRepository() = runTest {
        every { repository.getFeedPosts() } returns flowOf(PagingData.empty())

        val result = useCase().first()

        assertNotNull(result)
        verify { repository.getFeedPosts() }
    }
}
