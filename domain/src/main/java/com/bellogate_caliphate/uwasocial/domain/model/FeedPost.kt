package com.bellogate_caliphate.uwasocial.domain.model

data class FeedPost(
    val id: Long,
    val user: User,
    val title: String,
    val body: String,
    val imageUrl: String?,
    val relativeTime: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean
)
