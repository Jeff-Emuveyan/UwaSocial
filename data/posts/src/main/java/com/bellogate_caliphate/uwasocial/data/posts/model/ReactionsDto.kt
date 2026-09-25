package com.bellogate_caliphate.uwasocial.data.posts.model

import com.google.gson.annotations.SerializedName

data class ReactionsDto(
    @SerializedName("likes") val likes: Int = 0,
    @SerializedName("dislikes") val dislikes: Int = 0
)
