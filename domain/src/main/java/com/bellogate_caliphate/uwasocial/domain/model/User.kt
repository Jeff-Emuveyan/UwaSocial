package com.bellogate_caliphate.uwasocial.domain.model

data class User(
    val id: Long,
    val name: String,
    val avatarUrl: String?,
    val initials: String,
    val location: String
)
