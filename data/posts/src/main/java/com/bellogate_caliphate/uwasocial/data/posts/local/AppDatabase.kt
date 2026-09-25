package com.bellogate_caliphate.uwasocial.data.posts.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bellogate_caliphate.uwasocial.data.user.local.UserDao
import com.bellogate_caliphate.uwasocial.data.user.local.UserEntity

@Database(
    entities = [PostEntity::class, UserEntity::class, RemoteKeyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
