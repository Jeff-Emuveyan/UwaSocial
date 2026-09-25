package com.bellogate_caliphate.uwasocial.features.posts.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun PostHeader(
    userName: String,
    userAvatarUrl: String?,
    userInitials: String,
    location: String,
    relativeTime: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PostAvatar(avatarUrl = userAvatarUrl, initials = userInitials)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = userName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Text(
                text = "$location · $relativeTime",
                color = Color(0xFF9CA3AF),
                fontSize = 13.sp
            )
        }
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = "More options",
                tint = Color(0xFF9CA3AF)
            )
        }
    }
}

@Preview
@Composable
private fun PostHeaderPreview() {
    PostHeader(
        userName = "Amara Osei",
        userAvatarUrl = null,
        userInitials = "AO",
        location = "Accra, Ghana",
        relativeTime = "2 min ago"
    )
}
