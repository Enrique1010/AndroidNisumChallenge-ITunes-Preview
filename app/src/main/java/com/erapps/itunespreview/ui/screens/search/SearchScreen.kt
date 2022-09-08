package com.erapps.itunespreview.ui.screens.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.erapps.itunespreview.data.models.Album
import com.erapps.itunespreview.ui.screens.search.searchstate.SearchDisplay
import com.erapps.itunespreview.ui.screens.search.searchstate.SuggestionModel
import com.erapps.itunespreview.ui.screens.search.searchstate.rememberSearchState
import java.lang.System.currentTimeMillis

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    onAlbumClick: (Album?) -> Unit
) {
    val list = viewModel.albumListState.collectAsLazyPagingItems()
    val state = rememberSearchState(searchResults = list)

    viewModel.getAlbums()
    state.searchResults = list

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchBar(
            modifier = Modifier,
            query = state.query,
            onQueryChange = {
                state.query = it
            },
            onSearchFocusChange = { state.focused = it },
            searchByQuery = {
                viewModel.getSuggestions()
                viewModel.updateSearchText(state.query.text)
                viewModel.insertSuggestion(
                    SuggestionModel(
                        id = currentTimeMillis().toInt(),
                        suggestion = state.query.text
                    )
                )
            },
            onBack = { state.query = TextFieldValue(state.query.text) },
            searching = state.searching,
            focused = state.focused
        )
        when (state.searchDisplay) {
            SearchDisplay.InitialResults -> {
                AlbumsList(list = state.searchResults) { onAlbumClick(it) }
            }
            SearchDisplay.NoResults -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Oops!, Nothing finded, try search in the bar above and press find icon again.",
                        fontSize = 16.sp
                    )
                }
            }
            SearchDisplay.Suggestions -> {
                state.suggestions = viewModel.searchSuggestions.value
                if (state.suggestions.count() >= 5) {
                    viewModel.clearSuggestions()
                }
                LazyColumn {
                    items(state.suggestions) {
                        DropdownMenuItem(
                            onClick = {
                                viewModel.updateSearchText(it.suggestion)
                                state.query = TextFieldValue(it.suggestion)
                                viewModel.getAlbums()
                            }
                        ) {
                            Icon(imageVector = Icons.Default.History, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = it.suggestion)
                        }
                    }
                }
            }
            SearchDisplay.Results -> {
                AlbumsList(list = state.searchResults) { onAlbumClick(it) }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nothing to show, try search in the bar above.",
                fontSize = 16.sp
            )
        }
    }
}