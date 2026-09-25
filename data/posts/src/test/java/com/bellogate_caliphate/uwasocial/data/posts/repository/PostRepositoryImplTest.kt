package com.bellogate_caliphate.uwasocial.data.posts.repository

import com.bellogate_caliphate.uwasocial.data.posts.local.AppDatabase
import com.bellogate_caliphate.uwasocial.data.posts.local.PostDao
import com.bellogate_caliphate.uwasocial.data.posts.local.PostEntity
import com.bellogate_caliphate.uwasocial.data.posts.local.PostWithUserLocal
import com.bellogate_caliphate.uwasocial.data.posts.remote.PostApiService
import com.bellogate_caliphate.uwasocial.data.user.local.UserEntity
import com.bellogate_caliphate.uwasocial.data.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PostRepositoryImplTest {

    private val database: AppDatabase = mockk()
    private val postDao: PostDao = mockk(relaxed = true)
    private val postApiService: PostApiService = mockk()
    private val userRepository: UserRepository = mockk()
    private lateinit var repository: PostRepositoryImpl

    @Before
    fun setUp() {
        coEvery { database.postDao() } returns postDao
        repository = PostRepositoryImpl(database, postApiService, userRepository)
    }

    @Test
    fun mapToDomainModel_combinesPostAndUserCorrectly() {
        val userEntity = UserEntity(121, "John", "Doe", "avatarUrl", "Accra, Ghana")
        val postEntity = PostEntity(1, 121, "Title", "Body text", 10, 5, "imageUrl", false)
        val local = PostWithUserLocal(postEntity, userEntity)

        val feedPost = repository.mapToDomainModel(local)

        assertEquals(1L, feedPost.id)
        assertEquals("John Doe", feedPost.user.name)
        assertEquals("JD", feedPost.user.initials)
        assertEquals("Accra, Ghana", feedPost.user.location)
    }

    @Test
    fun toggleLike_updatesPostDaoCorrectly() = runTest {
        val existingPost = PostEntity(1, 121, "Title", "Body", 10, 5, "image", isLiked = false)
        coEvery { postDao.getPostById(1) } returns existingPost

        repository.toggleLike(1)

        coVerify { postDao.updateLikeStatus(1, true, 11) }
    }
}
