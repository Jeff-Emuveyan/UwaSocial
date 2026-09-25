package com.bellogate_caliphate.uwasocial.data.posts.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bellogate_caliphate.uwasocial.data.posts.local.AppDatabase
import com.bellogate_caliphate.uwasocial.data.posts.local.PostEntity
import com.bellogate_caliphate.uwasocial.data.posts.local.PostWithUserLocal
import com.bellogate_caliphate.uwasocial.data.posts.local.RemoteKeyEntity
import com.bellogate_caliphate.uwasocial.data.posts.model.PostDto
import com.bellogate_caliphate.uwasocial.data.posts.model.PostResponseDto
import com.bellogate_caliphate.uwasocial.data.posts.remote.PostApiService
import com.bellogate_caliphate.uwasocial.data.user.repository.UserRepository

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val database: AppDatabase,
    private val postApiService: PostApiService,
    private val userRepository: UserRepository
) : RemoteMediator<Int, PostWithUserLocal>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostWithUserLocal>
    ): MediatorResult {
        return try {
            val skip = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = getRemoteKeyClosestToPosition(state)
                    remoteKey?.nextSkip?.minus(state.config.pageSize) ?: 0
                }
                LoadType.PREPEND -> {
                    val remoteKey = getRemoteKeyForFirstItem(state)
                    val prevSkip = remoteKey?.prevSkip
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    prevSkip
                }
                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)
                    val nextSkip = remoteKey?.nextSkip
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    nextSkip
                }
            }

            val limit = state.config.pageSize
            val response = postApiService.getPosts(limit = limit, skip = skip)
            val endOfPagination = evaluateEndOfPagination(response)

            processAndSavePageData(loadType, skip, limit, response)

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, PostWithUserLocal>
    ): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { post ->
            database.remoteKeyDao().getRemoteKeyForPostId(post.post.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, PostWithUserLocal>
    ): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { post ->
            database.remoteKeyDao().getRemoteKeyForPostId(post.post.id)
        }
    }

    private suspend fun getRemoteKeyClosestToPosition(
        state: PagingState<Int, PostWithUserLocal>
    ): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.post?.id?.let { postId ->
                database.remoteKeyDao().getRemoteKeyForPostId(postId)
            }
        }
    }

    private fun evaluateEndOfPagination(response: PostResponseDto): Boolean {
        val nextOffset = response.skip + response.posts.size
        return nextOffset >= response.total || response.posts.isEmpty()
    }

    private suspend fun processAndSavePageData(
        loadType: LoadType,
        currentSkip: Int,
        pageSize: Int,
        response: PostResponseDto
    ) {
        val userIds = extractUniqueUserIds(response.posts)
        userRepository.getUsersByIds(userIds)

        val nextSkip = calculateNextSkip(currentSkip, pageSize, response)
        val prevSkip = calculatePrevSkip(currentSkip, pageSize)

        database.withTransaction {
            if (loadType == LoadType.REFRESH) {
                database.remoteKeyDao().clearRemoteKeys()
                database.postDao().clearPosts()
            }

            val remoteKeys = buildRemoteKeys(response.posts, prevSkip, nextSkip)
            val postEntities = buildPostEntities(response.posts)

            database.remoteKeyDao().insertAll(remoteKeys)
            database.postDao().insertPosts(postEntities)
        }
    }

    private fun extractUniqueUserIds(posts: List<PostDto>): List<Long> {
        return posts.map { it.userId }.distinct()
    }

    private fun calculateNextSkip(currentSkip: Int, pageSize: Int, response: PostResponseDto): Int? {
        val next = currentSkip + pageSize
        return if (next < response.total) next else null
    }

    private fun calculatePrevSkip(currentSkip: Int, pageSize: Int): Int? {
        val prev = currentSkip - pageSize
        return if (prev >= 0) prev else null
    }

    private fun buildRemoteKeys(
        posts: List<PostDto>,
        prevSkip: Int?,
        nextSkip: Int?
    ): List<RemoteKeyEntity> {
        return posts.map { post ->
            RemoteKeyEntity(
                postId = post.id,
                prevSkip = prevSkip,
                nextSkip = nextSkip
            )
        }
    }

    private fun buildPostEntities(posts: List<PostDto>): List<PostEntity> {
        return posts.map { dto ->
            val mediaUrl = determineMediaUrl(dto.id)
            PostEntity(
                id = dto.id,
                userId = dto.userId,
                title = dto.title,
                body = dto.body,
                likesCount = dto.extractLikesCount(),
                commentsCount = (dto.views % 45) + 3,
                imageUrl = mediaUrl,
                isLiked = false
            )
        }
    }

    private fun determineMediaUrl(postId: Long): String? {
        return if (postId % 3L != 0L) {
            "https://picsum.photos/seed/$postId/600/400"
        } else {
            null
        }
    }
}
