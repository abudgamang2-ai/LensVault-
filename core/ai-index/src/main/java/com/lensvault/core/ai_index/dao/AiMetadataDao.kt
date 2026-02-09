package com.lensvault.core.ai_index.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lensvault.core.ai_index.model.AiMetadataEntity

@Dao
interface AiMetadataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: AiMetadataEntity)

    @Query("SELECT * FROM ai_metadata WHERE mediaId = :mediaId")
    suspend fun getMetadata(mediaId: Long): AiMetadataEntity?

    @Query("SELECT * FROM ai_metadata WHERE extractedText LIKE '%' || :query || '%'")
    suspend fun searchByText(query: String): List<AiMetadataEntity>
}
