package com.bellogate_caliphate.uwasocial.data.user.model

import com.google.gson.annotations.SerializedName

internal data class AddressDto(
    @SerializedName("city") val city: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("country") val country: String?
)
