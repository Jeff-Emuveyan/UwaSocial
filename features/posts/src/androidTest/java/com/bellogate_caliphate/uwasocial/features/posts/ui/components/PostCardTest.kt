package com.bellogate_caliphate.uwasocial.features.posts.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bellogate_caliphate.uwasocial.domain.model.FeedPost
import com.bellogate_caliphate.uwasocial.domain.model.User
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun postCard_displaysPostContentAndUser() {
        val user = User(1, "Amara Osei", null, "AO", "Accra, Ghana")
        val post = FeedPost(
            id = 1,
            user = user,
            title = "Title",
            body = "This is a test post content",
            imageUrl = null,
            relativeTime = "2 min ago",
            likesCount = 142,
            commentsCount = 38,
            isLiked = false
        )

        composeTestRule.setContent {
            PostCard(post = post, onLikeClick = {})
        }

        composeTestRule.onNodeWithText("Amara Osei").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is a test post content").assertIsDisplayed()
        composeTestRule.onNodeWithText("142").assertIsDisplayed()
    }
}
