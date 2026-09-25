package com.bellogate_caliphate.uwasocial.domain.di

import com.bellogate_caliphate.uwasocial.domain.usecase.getfeedposts.GetFeedPostsUseCase
import com.bellogate_caliphate.uwasocial.domain.usecase.getfeedposts.GetFeedPostsUseCaseImpl
import com.bellogate_caliphate.uwasocial.domain.usecase.togglelikepost.ToggleLikePostUseCase
import com.bellogate_caliphate.uwasocial.domain.usecase.togglelikepost.ToggleLikePostUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindGetFeedPostsUseCase(
        impl: GetFeedPostsUseCaseImpl
    ): GetFeedPostsUseCase

    @Binds
    @Singleton
    abstract fun bindToggleLikePostUseCase(
        impl: ToggleLikePostUseCaseImpl
    ): ToggleLikePostUseCase
}
