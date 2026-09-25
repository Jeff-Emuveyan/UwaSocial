package com.bellogate_caliphate.uwasocial.data.posts.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PostDao {

    @Transaction
    @Query("SELECT * FROM posts ORDER BY id ASC")
    fun getPostsWithUserPaging(): PagingSource<Int, PostWithUserLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("UPDATE posts SET isLiked = :isLiked, likesCount = :newLikesCount WHERE id = :postId")
    suspend fun updateLikeStatus(postId: Long, isLiked: Boolean, newLikesCount: Int)

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Long): PostEntity?

    @Query("DELETE FROM posts")
    suspend fun clearPosts()
}
