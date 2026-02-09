package com.lensvault.core.ai_index.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_metadata")
data class AiMetadataEntity(
    @PrimaryKey val mediaId: Long,
    val faceCount: Int,
    val dominantColor: Int? = null,
    val extractedText: String? = null,
    val analyzedTimestamp: Long = System.currentTimeMillis()
)
