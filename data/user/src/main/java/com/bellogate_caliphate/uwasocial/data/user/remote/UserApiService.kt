package com.bellogate_caliphate.uwasocial.data.user.remote

import com.bellogate_caliphate.uwasocial.data.user.model.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

internal interface UserApiService {

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Long): UserDto
}
