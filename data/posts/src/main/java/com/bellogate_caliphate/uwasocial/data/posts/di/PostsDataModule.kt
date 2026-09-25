package com.bellogate_caliphate.uwasocial.data.posts.di

import android.content.Context
import androidx.room.Room
import com.bellogate_caliphate.uwasocial.data.posts.local.AppDatabase
import com.bellogate_caliphate.uwasocial.data.posts.local.PostDao
import com.bellogate_caliphate.uwasocial.data.posts.local.RemoteKeyDao
import com.bellogate_caliphate.uwasocial.data.posts.remote.PostApiService
import com.bellogate_caliphate.uwasocial.data.posts.repository.PostRepository
import com.bellogate_caliphate.uwasocial.data.posts.repository.PostRepositoryImpl
import com.bellogate_caliphate.uwasocial.data.user.local.UserDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PostsDataModule {

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository

    companion object {

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "uwa_social.db"
            ).fallbackToDestructiveMigration().build()
        }

        @Provides
        fun providePostDao(database: AppDatabase): PostDao {
            return database.postDao()
        }

        @Provides
        fun provideUserDao(database: AppDatabase): UserDao {
            return database.userDao()
        }

        @Provides
        fun provideRemoteKeyDao(database: AppDatabase): RemoteKeyDao {
            return database.remoteKeyDao()
        }

        @Provides
        @Singleton
        fun providePostApiService(retrofit: Retrofit): PostApiService {
            return retrofit.create(PostApiService::class.java)
        }
    }
}
