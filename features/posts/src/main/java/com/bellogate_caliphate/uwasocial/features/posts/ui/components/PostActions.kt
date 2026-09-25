package com.bellogate_caliphate.uwasocial.features.posts.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun PostActions(
    likesCount: Int,
    commentsCount: Int,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onLikeClick) {
            Icon(
                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (isLiked) Color(0xFFEF4444) else Color(0xFF9CA3AF)
            )
        }
        Text(
            text = "$likesCount",
            color = Color(0xFF9CA3AF),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(20.dp))

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.ModeComment,
                contentDescription = "Comments",
                tint = Color(0xFF9CA3AF)
            )
        }
        Text(
            text = "$commentsCount",
            color = Color(0xFF9CA3AF),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color(0xFF9CA3AF)
            )
        }
    }
}

@Preview
@Composable
private fun PostActionsPreview() {
    PostActions(
        likesCount = 142,
        commentsCount = 38,
        isLiked = true,
        onLikeClick = {}
    )
}
