package com.bellogate_caliphate.uwasocial.data.posts.remote

import com.bellogate_caliphate.uwasocial.data.posts.model.PostResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PostApiService {

    @GET("posts")
    suspend fun getPosts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): PostResponseDto
}
