package com.kai.padhelper.dagger

import com.kai.padhelper.data.repository.IPadSearchRepository
import com.kai.padhelper.data.repository.PadSearchRepository
import com.kai.padhelper.ui.adapters.PadSearchAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providePadSearchAdapter(): PadSearchAdapter {
        return PadSearchAdapter()
    }
}