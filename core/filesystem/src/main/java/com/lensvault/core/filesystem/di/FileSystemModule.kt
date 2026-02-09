package com.lensvault.core.filesystem.di

import com.lensvault.core.filesystem.FileSystemManager
import com.lensvault.core.filesystem.FileSystemManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FileSystemModule {

    @Binds
    abstract fun bindFileSystemManager(
        impl: FileSystemManagerImpl
    ): FileSystemManager
}
