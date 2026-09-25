package com.bellogate_caliphate.uwasocial.features.posts.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.bellogate_caliphate.uwasocial.domain.model.FeedPost
import com.bellogate_caliphate.uwasocial.domain.model.User
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class PostsFeedScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun feedList_rendersPostsAndScrollsToItem() {
        val sampleUser = User(1, "Amara Osei", null, "AO", "Accra, Ghana")
        val posts = (1..10).map { id ->
            FeedPost(
                id = id.toLong(),
                user = sampleUser,
                title = "Title $id",
                body = "Post body content number $id",
                imageUrl = if (id % 2 == 0) "https://picsum.photos/seed/$id/600/400" else null,
                relativeTime = "$id min ago",
                likesCount = 10 * id,
                commentsCount = id,
                isLiked = false
            )
        }

        composeTestRule.setContent {
            val pagingItems = flowOf(PagingData.from(posts)).collectAsLazyPagingItems()
            com.bellogate_caliphate.uwasocial.features.posts.ui.screen.PostsFeedScreenContent(
                pagingItems = pagingItems,
                isOffline = false,
                onLikeClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Post body content number 1", substring = true)
            .assertIsDisplayed()

        composeTestRule
            .onNode(hasScrollAction())
            .performScrollToNode(hasText("Post body content number 5", substring = true))

        composeTestRule
            .onNodeWithText("Post body content number 5", substring = true)
            .assertIsDisplayed()
    }
}
