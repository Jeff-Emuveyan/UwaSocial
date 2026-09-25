package com.bellogate_caliphate.uwasocial.data.user.repository

import com.bellogate_caliphate.uwasocial.data.user.local.UserDao
import com.bellogate_caliphate.uwasocial.data.user.local.UserEntity
import com.bellogate_caliphate.uwasocial.data.user.model.AddressDto
import com.bellogate_caliphate.uwasocial.data.user.model.UserDto
import com.bellogate_caliphate.uwasocial.data.user.remote.UserApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private val userDao: UserDao = mockk(relaxed = true)
    private val userApiService: UserApiService = mockk()
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setUp() {
        repository = UserRepositoryImpl(userDao, userApiService)
    }

    @Test
    fun getUsersByIds_returnsCachedUsers_whenAllAreCached() = runTest {
        val cachedEntity = UserEntity(16, "Mason", "Wood", "url", "Dallas, Texas")
        coEvery { userDao.getUsersByIds(listOf(16L)) } returns listOf(cachedEntity)

        val result = repository.getUsersByIds(listOf(16L))

        assertEquals(1, result.size)
        assertEquals("Mason", result[16L]?.firstName)
        coVerify(exactly = 0) { userApiService.getUserById(any()) }
    }

    @Test
    fun getUsersByIds_fetchesRemoteUser_whenNotCached() = runTest {
        coEvery { userDao.getUsersByIds(listOf(16L)) } returns emptyList()
        val dto = UserDto(16L, "Mason", "Wood", "url", AddressDto("Dallas", "Texas", "USA"))
        coEvery { userApiService.getUserById(16L) } returns dto

        val result = repository.getUsersByIds(listOf(16L))

        assertEquals(1, result.size)
        assertEquals("Mason", result[16L]?.firstName)
        coVerify(exactly = 1) { userDao.insertUsers(any()) }
    }
}
