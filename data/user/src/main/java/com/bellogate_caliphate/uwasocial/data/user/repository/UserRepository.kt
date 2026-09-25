package com.bellogate_caliphate.uwasocial.data.user.repository

import com.bellogate_caliphate.uwasocial.data.user.local.UserEntity

interface UserRepository {
    suspend fun getUsersByIds(userIds: List<Long>): Map<Long, UserEntity>
}
