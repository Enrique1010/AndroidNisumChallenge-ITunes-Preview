package com.erapps.itunespreview.data.source.remote.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.erapps.itunespreview.data.api.service.ITunesApiService
import com.erapps.itunespreview.data.models.Album
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

interface ISearchRemoteDataSource {
    fun getAlbumsByTerms(term: String): Flow<PagingData<Album>>
}

class SearchRemoteDataSource(
    private val iTunesApiService: ITunesApiService,
    private val ioDispatcher: CoroutineDispatcher
) : ISearchRemoteDataSource {

    override fun getAlbumsByTerms(term: String): Flow<PagingData<Album>> {
        val pageSize = 20
        return Pager(
            PagingConfig(pageSize = pageSize)
        ) {
            SearchPagingSource(iTunesApiService, term)
        }.flow.flowOn(ioDispatcher)
    }
}