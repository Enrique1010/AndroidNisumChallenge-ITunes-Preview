package com.erapps.itunespreview.data.source.local.search

import com.erapps.itunespreview.data.room.SuggestionsDao
import com.erapps.itunespreview.ui.screens.search.searchstate.SuggestionModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ISearchLocalDataSource {
    suspend fun getSuggestions(): MutableList<SuggestionModel>
    suspend fun insertSuggestions(suggestionModel: SuggestionModel)
    suspend fun clearSuggestions()
    suspend fun deleteDuplicates()
}

class SearchLocalDataSource @Inject constructor(
    private val suggestionsDao: SuggestionsDao,
    private val ioDispatcher: CoroutineDispatcher
) : ISearchLocalDataSource {

    override suspend fun getSuggestions(): MutableList<SuggestionModel> = withContext(ioDispatcher) {
        return@withContext suggestionsDao.getSuggestions()
    }

    override suspend fun insertSuggestions(suggestionModel: SuggestionModel) =
        withContext(ioDispatcher) {
            suggestionsDao.insertSuggestion(suggestionModel)
        }

    override suspend fun clearSuggestions() = withContext(ioDispatcher) {
        suggestionsDao.clearSuggestions()
    }

    override suspend fun deleteDuplicates() {
        suggestionsDao.deleteDuplicates()
    }
}