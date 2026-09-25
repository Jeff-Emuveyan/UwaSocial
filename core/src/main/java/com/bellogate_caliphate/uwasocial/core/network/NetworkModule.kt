package com.bellogate_caliphate.uwasocial.core.network

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindNetworkObserver(
        networkObserverImpl: NetworkObserverImpl
    ): NetworkObserver
}
