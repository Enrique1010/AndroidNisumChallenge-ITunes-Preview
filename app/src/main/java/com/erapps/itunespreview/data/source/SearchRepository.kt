package com.erapps.itunespreview.data.source

import androidx.paging.PagingData
import com.erapps.itunespreview.data.models.Album
import com.erapps.itunespreview.data.source.local.search.ISearchLocalDataSource
import com.erapps.itunespreview.data.source.remote.search.ISearchRemoteDataSource
import com.erapps.itunespreview.ui.screens.search.searchstate.SuggestionModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

interface ISearchRepository {
    //remote
    fun getAlbumsByTerm(term: String): Flow<PagingData<Album>>

    //local
    suspend fun getSuggestions(): MutableList<SuggestionModel>
    suspend fun insertSuggestions(suggestionModel: SuggestionModel)
    suspend fun clearSuggestions()
    suspend fun deleteDuplicates()
}

class SearchRepository(
    private val searchRemoteDataSource: ISearchRemoteDataSource,
    private val searchLocalDataSource: ISearchLocalDataSource,
    private val defaultDispatcher: CoroutineDispatcher
) : ISearchRepository {

    override fun getAlbumsByTerm(term: String): Flow<PagingData<Album>> {
        return searchRemoteDataSource.getAlbumsByTerms(term).flowOn(defaultDispatcher)
    }

    override suspend fun getSuggestions(): MutableList<SuggestionModel> {
        return searchLocalDataSource.getSuggestions()
    }

    override suspend fun insertSuggestions(suggestionModel: SuggestionModel) {
        searchLocalDataSource.insertSuggestions(suggestionModel)
    }

    override suspend fun clearSuggestions() {
        searchLocalDataSource.clearSuggestions()
    }

    override suspend fun deleteDuplicates() {
        searchLocalDataSource.deleteDuplicates()
    }
}