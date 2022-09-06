package com.erapps.itunespreview.ui.screens.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.erapps.itunespreview.data.models.Album
import com.erapps.itunespreview.data.source.remote.search.ISearchRepository
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

    private val _termQuery = mutableStateOf("")
    val termQuery: State<String> = _termQuery

    fun updateSearchText(query: String) {
        _termQuery.value = query
    }

    fun getAlbums() = viewModelScope.launch {
        //val query = term.replace(" ", "+")
        searchRepository.getAlbumsByTerm(_termQuery.value)
            .cachedIn(viewModelScope)
            .collect { albums ->
                _albumListState.update { albums }
            }
    }

}