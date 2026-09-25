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
            val skip = calculateSkip(loadType, state) ?: return MediatorResult.Success(
                endOfPaginationReached = true
            )

            val limit = state.config.pageSize
            val response = postApiService.getPosts(limit = limit, skip = skip)
            val endOfPagination = evaluateEndOfPagination(response)

            processAndSavePageData(loadType, skip, limit, response)

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun calculateSkip(
        loadType: LoadType,
        state: PagingState<Int, PostWithUserLocal>
    ): Int? {
        return when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> getSkipForPrepend(state)
            LoadType.APPEND -> getSkipForAppend(state)
        }
    }

    private suspend fun getSkipForPrepend(
        state: PagingState<Int, PostWithUserLocal>
    ): Int? {
        val firstItem = state.firstItemOrNull() ?: return null
        val remoteKey = database.remoteKeyDao().getRemoteKeyForPostId(firstItem.post.id)
        return remoteKey?.prevSkip
    }

    private suspend fun getSkipForAppend(
        state: PagingState<Int, PostWithUserLocal>
    ): Int? {
        val lastItem = state.lastItemOrNull() ?: return null
        val remoteKey = database.remoteKeyDao().getRemoteKeyForPostId(lastItem.post.id)
        return remoteKey?.nextSkip
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
