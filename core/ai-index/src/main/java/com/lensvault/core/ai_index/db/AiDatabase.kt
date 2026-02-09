package com.lensvault.core.ai_index.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lensvault.core.ai_index.dao.AiMetadataDao
import com.lensvault.core.ai_index.model.AiMetadataEntity

@Database(entities = [AiMetadataEntity::class], version = 1, exportSchema = false)
abstract class AiDatabase : RoomDatabase() {
    abstract fun aiMetadataDao(): AiMetadataDao
}
