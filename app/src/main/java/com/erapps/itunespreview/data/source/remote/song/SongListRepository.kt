package com.erapps.itunespreview.data.source.remote.song

import com.erapps.itunespreview.data.models.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

interface ISongListRepository {
    fun getSongListByAlbumId(albumId: Long): Flow<List<Song>>
}

class SongListRepository(
    private val songListRemoteDataSource: ISongListRemoteDataSource,
    private val defaultDispatcher: CoroutineDispatcher
) : ISongListRepository {

    override fun getSongListByAlbumId(albumId: Long): Flow<List<Song>> {
        return songListRemoteDataSource.getSongsByAlbumId(albumId).flowOn(defaultDispatcher)
    }
}