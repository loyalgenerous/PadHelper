package com.kai.padhelper.dagger

import com.kai.padhelper.data.remote.HtmlFetcher
import com.kai.padhelper.data.remote.JsoupHtmlFetcher
import com.kai.padhelper.data.repository.IPadSearchRepository
import com.kai.padhelper.data.repository.PadSearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    companion object {
        @Provides
        @Singleton
        fun provideHtmlFetcher(): HtmlFetcher {
            return JsoupHtmlFetcher()
        }
    }

    @Binds
    @Singleton
    abstract fun bindPadSearchRepository(
        padSearchRepository: PadSearchRepository
    ): IPadSearchRepository
}