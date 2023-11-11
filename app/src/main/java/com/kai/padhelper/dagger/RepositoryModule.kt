package com.kai.padhelper.dagger

import com.kai.padhelper.data.repository.PadSearchRepository
import com.kai.padhelper.ui.adapters.PadSearchAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun providePadSearchRepository(): PadSearchRepository {
        return PadSearchRepository
    }

    @Provides
    fun providePadSearchAdapter(): PadSearchAdapter {
        return PadSearchAdapter()
    }
}