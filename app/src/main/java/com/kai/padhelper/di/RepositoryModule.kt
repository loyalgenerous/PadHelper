package com.kai.padhelper.di

import android.app.Application
import com.kai.padhelper.data.db.AppDataBase
import com.kai.padhelper.data.remote.HtmlFetcher
import com.kai.padhelper.data.remote.JsoupHtmlFetcher
import com.kai.padhelper.data.repository.IRepository
import com.kai.padhelper.data.repository.Repository
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
        fun provideAppDataBase(app: Application): AppDataBase {
            return AppDataBase(app)
        }
    }

    @Binds
    @Singleton
    abstract fun provideHtmlFetcher(
        jsoupHtmlFetcher: JsoupHtmlFetcher
    ): HtmlFetcher

    @Binds
    @Singleton
    abstract fun bindPadSearchRepository(
        padSearchRepository: Repository
    ): IRepository
}