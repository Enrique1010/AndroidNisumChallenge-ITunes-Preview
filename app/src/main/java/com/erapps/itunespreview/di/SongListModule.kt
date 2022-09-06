package com.erapps.itunespreview.di

import com.erapps.itunespreview.data.api.service.ITunesApiService
import com.erapps.itunespreview.data.source.remote.song.ISongListRemoteDataSource
import com.erapps.itunespreview.data.source.remote.song.ISongListRepository
import com.erapps.itunespreview.data.source.remote.song.SongListRemoteDataSource
import com.erapps.itunespreview.data.source.remote.song.SongListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SongListModule {

    @Singleton
    @Provides
    fun provideSongListRemoteDataSource(
        iTunesApiService: ITunesApiService,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): ISongListRemoteDataSource {
        return SongListRemoteDataSource(iTunesApiService, coroutineDispatcher)
    }

    @Singleton
    @Provides
    fun provideSongListRepository(
        iSongListRemoteDataSource: ISongListRemoteDataSource,
        @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher
    ): ISongListRepository {
        return SongListRepository(iSongListRemoteDataSource, coroutineDispatcher)
    }

}