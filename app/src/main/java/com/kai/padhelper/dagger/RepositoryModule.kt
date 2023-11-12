package com.kai.padhelper.dagger

import com.kai.padhelper.data.repository.IPadSearchRepository
import com.kai.padhelper.data.repository.PadSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPadSearchRepository(
        padSearchRepository: PadSearchRepository
    ): IPadSearchRepository
}