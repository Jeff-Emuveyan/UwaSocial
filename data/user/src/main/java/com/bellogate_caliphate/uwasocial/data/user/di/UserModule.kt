package com.bellogate_caliphate.uwasocial.data.user.di

import com.bellogate_caliphate.uwasocial.data.user.remote.UserApiService
import com.bellogate_caliphate.uwasocial.data.user.repository.UserRepository
import com.bellogate_caliphate.uwasocial.data.user.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UserModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    companion object {
        @Provides
        @Singleton
        fun provideUserApiService(retrofit: Retrofit): UserApiService {
            return retrofit.create(UserApiService::class.java)
        }
    }
}
