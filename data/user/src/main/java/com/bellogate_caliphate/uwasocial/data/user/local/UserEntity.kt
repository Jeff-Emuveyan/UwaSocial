package com.bellogate_caliphate.uwasocial.data.user.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    val firstName: String,
    val lastName: String,
    val image: String?,
    val location: String
)
