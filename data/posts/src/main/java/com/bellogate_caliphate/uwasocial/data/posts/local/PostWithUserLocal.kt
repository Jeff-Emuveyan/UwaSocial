package com.bellogate_caliphate.uwasocial.data.posts.local

import androidx.room.Embedded
import androidx.room.Relation
import com.bellogate_caliphate.uwasocial.data.user.local.UserEntity

data class PostWithUserLocal(
    @Embedded val post: PostEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val user: UserEntity?
)
