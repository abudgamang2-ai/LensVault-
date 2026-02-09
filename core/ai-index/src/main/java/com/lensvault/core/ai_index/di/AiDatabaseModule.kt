package com.lensvault.core.ai_index.di

import android.content.Context
import androidx.room.Room
import com.lensvault.core.ai_index.dao.AiMetadataDao
import com.lensvault.core.ai_index.db.AiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiDatabaseModule {

    @Provides
    @Singleton
    fun provideAiDatabase(
        @ApplicationContext context: Context
    ): AiDatabase {
        return Room.databaseBuilder(
            context,
            AiDatabase::class.java,
            "lensvault-ai.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideAiMetadataDao(database: AiDatabase): AiMetadataDao {
        return database.aiMetadataDao()
    }
}
