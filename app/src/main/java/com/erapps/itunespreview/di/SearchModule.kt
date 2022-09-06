package com.erapps.itunespreview.di

import com.erapps.itunespreview.data.api.service.ITunesApiService
import com.erapps.itunespreview.data.source.remote.ISearchRemoteDataSource
import com.erapps.itunespreview.data.source.remote.ISearchRepository
import com.erapps.itunespreview.data.source.remote.SearchRemoteDataSource
import com.erapps.itunespreview.data.source.remote.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {

    @Singleton
    @Provides
    fun provideSearchRemoteDataSource(
        iTunesApiService: ITunesApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): ISearchRemoteDataSource {
        return SearchRemoteDataSource(iTunesApiService, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideSearchRepository(
        searchRemoteDataSource: ISearchRemoteDataSource,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): ISearchRepository {
        return SearchRepository(searchRemoteDataSource, defaultDispatcher)
    }
}