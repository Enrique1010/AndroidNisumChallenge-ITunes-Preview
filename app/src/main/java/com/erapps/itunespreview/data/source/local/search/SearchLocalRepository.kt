package com.erapps.itunespreview.data.source.local.search

import com.erapps.itunespreview.ui.screens.search.searchstate.SuggestionModel
import javax.inject.Inject

interface ISearchLocalRepository{
    suspend fun getSuggestions(): MutableList<SuggestionModel>
    suspend fun insertSuggestions(suggestionModel: SuggestionModel)
    suspend fun clearSuggestions()
    suspend fun deleteDuplicates()
}

class SearchLocalRepository @Inject constructor(
    private val searchLocalDataSource: ISearchLocalDataSource,
): ISearchLocalRepository {

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