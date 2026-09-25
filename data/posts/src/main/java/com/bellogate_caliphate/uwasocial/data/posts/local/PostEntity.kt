package com.bellogate_caliphate.uwasocial.data.posts.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Long,
    val userId: Long,
    val title: String,
    val body: String,
    val likesCount: Int,
    val commentsCount: Int,
    val imageUrl: String?,
    val isLiked: Boolean = false
)
