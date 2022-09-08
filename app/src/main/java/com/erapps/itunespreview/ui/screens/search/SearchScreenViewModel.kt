package com.erapps.itunespreview.ui.screens.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.erapps.itunespreview.data.models.Album
import com.erapps.itunespreview.data.source.ISearchRepository
import com.erapps.itunespreview.ui.screens.search.searchstate.SuggestionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchRepository: ISearchRepository
) : ViewModel() {

    private val _albumListState = MutableStateFlow(PagingData.empty<Album>())
    val albumListState = _albumListState.asStateFlow()

    private val _searchSuggestions = MutableStateFlow<MutableList<SuggestionModel>>(mutableListOf())
    val searchSuggestions = _searchSuggestions.asStateFlow()

    private val termQuery = mutableStateOf("")

    fun updateSearchText(query: String) {
        termQuery.value = query
    }

    fun getAlbums() = viewModelScope.launch {
        searchRepository.getAlbumsByTerm(termQuery.value)
            .cachedIn(viewModelScope)
            .collect { albums ->
                _albumListState.update { albums }
            }
    }

    fun insertSuggestion(suggestionModel: SuggestionModel) = viewModelScope.launch {
        searchRepository.insertSuggestions(suggestionModel)
    }

    fun getSuggestions() = viewModelScope.launch {
        _searchSuggestions.update { searchRepository.getSuggestions() }
    }

    fun clearSuggestions() = viewModelScope.launch {
        searchRepository.clearSuggestions()
    }

}