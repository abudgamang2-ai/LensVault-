package com.lensvault.core.data.di

import com.lensvault.core.data.repository.MediaRepositoryImpl
import com.lensvault.core.domain.repository.MediaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindMediaRepository(
        impl: MediaRepositoryImpl
    ): MediaRepository

    @Binds
    abstract fun bindAiRepository(
        impl: AiRepositoryImpl
    ): AiRepository
}
