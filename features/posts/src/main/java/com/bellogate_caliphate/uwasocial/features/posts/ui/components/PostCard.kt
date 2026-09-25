package com.bellogate_caliphate.uwasocial.features.posts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bellogate_caliphate.uwasocial.domain.model.FeedPost
import com.bellogate_caliphate.uwasocial.domain.model.User

@Composable
fun PostCard(
    post: FeedPost,
    onLikeClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF111827))
            .padding(16.dp)
    ) {
        PostHeader(
            userName = post.user.name,
            userAvatarUrl = post.user.avatarUrl,
            userInitials = post.user.initials,
            location = post.user.location,
            relativeTime = post.relativeTime
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = post.body,
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        if (!post.imageUrl.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            PostMedia(imageUrl = post.imageUrl)
        }

        Spacer(modifier = Modifier.height(12.dp))

        PostActions(
            likesCount = post.likesCount,
            commentsCount = post.commentsCount,
            isLiked = post.isLiked,
            onLikeClick = { onLikeClick(post.id) }
        )
    }
}

@Preview
@Composable
private fun PostCardPreview() {
    val sampleUser = User(
        id = 1,
        name = "Amara Osei",
        avatarUrl = null,
        initials = "AO",
        location = "Accra, Ghana"
    )
    val samplePost = FeedPost(
        id = 1,
        user = sampleUser,
        title = "Sample Post",
        body = "Finally hit 1000 followers on here 🎉 Thank you all for the support.",
        imageUrl = "https://picsum.photos/seed/1/600/400",
        relativeTime = "2 min ago",
        likesCount = 142,
        commentsCount = 38,
        isLiked = false
    )
    PostCard(post = samplePost, onLikeClick = {})
}
