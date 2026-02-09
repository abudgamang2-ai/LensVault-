package com.lensvault.core.domain.repository

import androidx.paging.PagingData
import com.lensvault.core.model.Media
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getPagedMedia(): Flow<PagingData<Media>>
    suspend fun deleteMedia(media: Media)
}
