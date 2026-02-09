package com.lensvault.core.filesystem

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lensvault.core.model.Media
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface FileSystemManager {
    fun getMedia PagingSource(): PagingSource<Int, Media>
    suspend fun delete(media: Media): Boolean
}

@Singleton
class FileSystemManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FileSystemManager {

    override fun getMediaPagingSource(): PagingSource<Int, Media> {
        return MediaStorePagingSource(context)
    }

    override suspend fun delete(media: Media): Boolean {
        // Deletion logic would go here (requires handling recoverable security exceptions on Android 10+)
        return true 
    }
}

class MediaStorePagingSource(
    private val context: Context
) : PagingSource<Int, Media>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Media> {
        return withContext(Dispatchers.IO) {
            val offset = params.key ?: 0
            val loadSize = params.loadSize
            
            val mediaList = mutableListOf<Media>()
            
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.WIDTH,
                MediaStore.Files.FileColumns.HEIGHT,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DURATION
            )

            val queryUri = MediaStore.Files.getContentUri("external")
            
            val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"
            val selectionArgs = arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            )
            
            val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC LIMIT $loadSize OFFSET $offset"

            context.contentResolver.query(
                queryUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
                val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
                val mediaTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DURATION)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val path = cursor.getString(pathColumn) ?: ""
                    val name = cursor.getString(nameColumn) ?: ""
                    val dateAdded = cursor.getLong(dateAddedColumn)
                    val mimeType = cursor.getString(mimeTypeColumn) ?: ""
                    val mediaType = cursor.getInt(mediaTypeColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)
                    val size = cursor.getLong(sizeColumn)
                    val duration = cursor.getLong(durationColumn) // 0 if not video or null
                    
                    val contentUri = ContentUris.withAppendedId(queryUri, id).toString()
                    
                    mediaList.add(
                        Media(
                            id = id,
                            uri = contentUri,
                            path = path,
                            name = name,
                            dateAdded = dateAdded,
                            mimeType = mimeType,
                            width = width,
                            height = height,
                            size = size,
                            duration = duration,
                            isVideo = mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                        )
                    )
                }
            }

            val nextKey = if (mediaList.isEmpty()) null else offset + loadSize
            
            LoadResult.Page(
                data = mediaList,
                prevKey = if (offset == 0) null else offset - loadSize,
                nextKey = nextKey
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Media>): Int? {
        return state.anchorPosition
    }
}
