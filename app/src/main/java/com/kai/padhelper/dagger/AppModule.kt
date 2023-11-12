package com.kai.padhelper.dagger

import com.kai.padhelper.ui.adapters.SearchAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providePadSearchAdapter(): SearchAdapter {
        return SearchAdapter()
    }
}