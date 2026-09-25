package com.bellogate_caliphate.uwasocial.data.posts.model

import com.google.gson.annotations.SerializedName

data class PostResponseDto(
    @SerializedName("posts") val posts: List<PostDto>,
    @SerializedName("total") val total: Int,
    @SerializedName("skip") val skip: Int,
    @SerializedName("limit") val limit: Int
)
