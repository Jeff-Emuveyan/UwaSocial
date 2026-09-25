package com.bellogate_caliphate.uwasocial.data.posts.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bellogate_caliphate.uwasocial.core.utils.TimeUtils
import com.bellogate_caliphate.uwasocial.data.posts.local.AppDatabase
import com.bellogate_caliphate.uwasocial.data.posts.local.PostWithUserLocal
import com.bellogate_caliphate.uwasocial.data.posts.mediator.PostRemoteMediator
import com.bellogate_caliphate.uwasocial.data.posts.remote.PostApiService
import com.bellogate_caliphate.uwasocial.data.user.local.UserEntity
import com.bellogate_caliphate.uwasocial.data.user.repository.UserRepository
import com.bellogate_caliphate.uwasocial.domain.model.FeedPost
import com.bellogate_caliphate.uwasocial.domain.model.User
import com.bellogate_caliphate.uwasocial.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PostRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val postApiService: PostApiService,
    private val userRepository: UserRepository
) : PostRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getFeedPosts(): Flow<PagingData<FeedPost>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            remoteMediator = PostRemoteMediator(
                database = database,
                postApiService = postApiService,
                userRepository = userRepository
            ),
            pagingSourceFactory = { database.postDao().getPostsWithUserPaging() }
        ).flow.map { pagingData ->
            pagingData.map { postWithUser ->
                mapToDomainModel(postWithUser)
            }
        }
    }

    override suspend fun toggleLike(postId: Long) {
        val currentPost = database.postDao().getPostById(postId) ?: return
        val newIsLiked = !currentPost.isLiked
        val newLikesCount = if (newIsLiked) currentPost.likesCount + 1 else currentPost.likesCount - 1
        database.postDao().updateLikeStatus(postId, newIsLiked, newLikesCount.coerceAtLeast(0))
    }

    internal fun mapToDomainModel(local: PostWithUserLocal): FeedPost {
        val userDomain = mapUserDomain(local.user, local.post.userId)
        val relativeTime = TimeUtils.formatRelativeTime(local.post.id)

        return FeedPost(
            id = local.post.id,
            user = userDomain,
            title = local.post.title,
            body = local.post.body,
            imageUrl = local.post.imageUrl,
            relativeTime = relativeTime,
            likesCount = local.post.likesCount,
            commentsCount = local.post.commentsCount,
            isLiked = local.post.isLiked
        )
    }

    private fun mapUserDomain(userEntity: UserEntity?, userId: Long): User {
        if (userEntity == null) {
            return User(
                id = userId,
                name = "User #$userId",
                avatarUrl = null,
                initials = "U",
                location = "Unknown Location"
            )
        }

        val fullName = "${userEntity.firstName} ${userEntity.lastName}".trim()
        val initials = extractInitials(userEntity.firstName, userEntity.lastName)

        return User(
            id = userEntity.id,
            name = fullName.ifEmpty { "User #${userEntity.id}" },
            avatarUrl = userEntity.image,
            initials = initials,
            location = userEntity.location
        )
    }

    private fun extractInitials(firstName: String, lastName: String): String {
        val firstChar = firstName.firstOrNull()?.uppercaseChar() ?: ""
        val lastChar = lastName.firstOrNull()?.uppercaseChar() ?: ""
        val combined = "$firstChar$lastChar"
        return combined.ifEmpty { "U" }
    }
}
