package com.lensvault.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lensvault.core.domain.repository.MediaRepository
import com.lensvault.core.filesystem.FileSystemManager
import com.lensvault.core.model.Media
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val fileSystemManager: FileSystemManager
) : MediaRepository {

    override fun getPagedMedia(): Flow<PagingData<Media>> {
        return Pager(
            config = PagingConfig(
                pageSize = 60,
                enablePlaceholders = true,
                prefetchDistance = 4
            ),
            pagingSourceFactory = { fileSystemManager.getMediaPagingSource() }
        ).flow
    }

    override suspend fun deleteMedia(media: Media) {
        fileSystemManager.delete(media)
    }
}
