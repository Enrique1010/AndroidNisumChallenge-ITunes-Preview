package com.erapps.itunespreview.data.source.remote

import androidx.paging.PagingData
import com.erapps.itunespreview.data.models.Album
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

interface ISearchRepository {
    fun getAlbumsByTerm(term: String): Flow<PagingData<Album>>
}

class SearchRepository(
    private val searchRepository: ISearchRemoteDataSource,
    private val defaultDispatcher: CoroutineDispatcher
) : ISearchRepository {

    override fun getAlbumsByTerm(term: String): Flow<PagingData<Album>> {
        return searchRepository.getAlbumsByTerms(term).flowOn(defaultDispatcher)
    }
}