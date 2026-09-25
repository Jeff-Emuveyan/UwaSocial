package com.bellogate_caliphate.uwasocial.data.user.repository

import com.bellogate_caliphate.uwasocial.data.user.local.UserDao
import com.bellogate_caliphate.uwasocial.data.user.local.UserEntity
import com.bellogate_caliphate.uwasocial.data.user.model.UserDto
import com.bellogate_caliphate.uwasocial.data.user.remote.UserApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun getUsersByIds(userIds: List<Long>): Map<Long, UserEntity> {
        val distinctIds = userIds.distinct()
        if (distinctIds.isEmpty()) return emptyMap()

        val cachedUsersMap = fetchCachedUsers(distinctIds)
        val missingIds = findMissingUserIds(distinctIds, cachedUsersMap)

        if (missingIds.isEmpty()) {
            return cachedUsersMap
        }

        val newlyFetchedUsers = fetchAndCacheRemoteUsers(missingIds)
        return cachedUsersMap + newlyFetchedUsers
    }

    private suspend fun fetchCachedUsers(userIds: List<Long>): Map<Long, UserEntity> {
        return userDao.getUsersByIds(userIds).associateBy { it.id }
    }

    private fun findMissingUserIds(
        allIds: List<Long>,
        cachedMap: Map<Long, UserEntity>
    ): List<Long> {
        return allIds.filterNot { cachedMap.containsKey(it) }
    }

    private suspend fun fetchAndCacheRemoteUsers(missingIds: List<Long>): Map<Long, UserEntity> =
        coroutineScope {
            val fetchedUsers = missingIds.map { userId ->
                async { fetchSingleRemoteUser(userId) }
            }.awaitAll().filterNotNull()

            if (fetchedUsers.isNotEmpty()) {
                userDao.insertUsers(fetchedUsers)
            }

            fetchedUsers.associateBy { it.id }
        }

    private suspend fun fetchSingleRemoteUser(userId: Long): UserEntity? {
        return try {
            val dto = userApiService.getUserById(userId)
            mapDtoToEntity(dto)
        } catch (_: Exception) {
            null
        }
    }

    private fun mapDtoToEntity(dto: UserDto): UserEntity {
        val locationString = buildLocationString(dto)
        return UserEntity(
            id = dto.id,
            firstName = dto.firstName,
            lastName = dto.lastName,
            image = dto.image,
            location = locationString
        )
    }

    private fun buildLocationString(dto: UserDto): String {
        val city = dto.address?.city
        val state = dto.address?.state
        return when {
            !city.isNullOrEmpty() && !state.isNullOrEmpty() -> "$city, $state"
            !city.isNullOrEmpty() -> city
            !state.isNullOrEmpty() -> state
            else -> "Unknown Location"
        }
    }
}
