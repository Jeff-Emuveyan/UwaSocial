package com.bellogate_caliphate.uwasocial.data.posts.model

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("reactions") val reactions: Any?,
    @SerializedName("views") val views: Int = 0,
    @SerializedName("userId") val userId: Long
) {
    internal fun extractLikesCount(): Int {
        return when (reactions) {
            is Map<*, *> -> (reactions["likes"] as? Number)?.toInt() ?: 0
            is Number -> reactions.toInt()
            else -> 0
        }
    }
}
