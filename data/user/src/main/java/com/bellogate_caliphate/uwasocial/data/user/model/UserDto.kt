package com.bellogate_caliphate.uwasocial.data.user.model

import com.google.gson.annotations.SerializedName

internal data class UserDto(
    @SerializedName("id") val id: Long,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("image") val image: String?,
    @SerializedName("address") val address: AddressDto?
)
