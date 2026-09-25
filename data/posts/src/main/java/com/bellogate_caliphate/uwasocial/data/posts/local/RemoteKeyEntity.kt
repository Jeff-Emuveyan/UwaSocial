package com.bellogate_caliphate.uwasocial.data.posts.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val postId: Long,
    val prevSkip: Int?,
    val nextSkip: Int?
)
