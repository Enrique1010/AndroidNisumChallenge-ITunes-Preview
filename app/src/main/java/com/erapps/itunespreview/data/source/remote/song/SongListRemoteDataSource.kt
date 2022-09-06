package com.erapps.itunespreview.data.source.remote.song

import com.erapps.itunespreview.data.api.NetworkResponse
import com.erapps.itunespreview.data.api.service.ITunesApiService
import com.erapps.itunespreview.data.models.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface ISongListRemoteDataSource {
    fun getSongsByAlbumId(albumId: Long): Flow<List<Song>>
}

class SongListRemoteDataSource(
    private val iTunesApiService: ITunesApiService,
    private val ioDispatcher: CoroutineDispatcher
) : ISongListRemoteDataSource {

    override fun getSongsByAlbumId(albumId: Long): Flow<List<Song>> {
        return flow {
            when (val request = iTunesApiService.getSongsByAlbumId(albumId)) {
                is NetworkResponse.ApiError -> emit(emptyList())
                is NetworkResponse.NetworkError -> emit(emptyList())
                is NetworkResponse.Success -> emit(request.body!!.results)
                is NetworkResponse.UnknownError -> emit(emptyList())
            }
        }.flowOn(ioDispatcher)
    }
}