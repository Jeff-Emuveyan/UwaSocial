package com.bellogate_caliphate.uwasocial.features.posts.ui.viewmodel

import androidx.paging.PagingData
import com.bellogate_caliphate.uwasocial.core.network.NetworkObserver
import com.bellogate_caliphate.uwasocial.domain.usecase.getfeedposts.GetFeedPostsUseCase
import com.bellogate_caliphate.uwasocial.domain.usecase.togglelikepost.ToggleLikePostUseCase
import com.bellogate_caliphate.uwasocial.features.posts.ui.state.PostsUiState
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getFeedPostsUseCase: GetFeedPostsUseCase = mockk()
    private val toggleLikePostUseCase: ToggleLikePostUseCase = mockk(relaxed = true)
    private val networkObserver: NetworkObserver = mockk()
    private lateinit var viewModel: PostsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getFeedPostsUseCase() } returns flowOf(PagingData.empty())
        every { networkObserver.observeNetworkConnectivity() } returns flowOf(true)

        viewModel = PostsViewModel(
            getFeedPostsUseCase,
            toggleLikePostUseCase,
            networkObserver
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun toggleLike_invokesUseCase() = runTest {
        viewModel.toggleLike(10L)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { toggleLikePostUseCase(10L) }
    }

    @Test
    fun setUiState_updatesStateFlow() {
        viewModel.setUiState(PostsUiState.Error("Failed"))

        assertEquals(PostsUiState.Error("Failed"), viewModel.uiState.value)
    }
}
