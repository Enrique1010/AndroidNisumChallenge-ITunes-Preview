package com.erapps.itunespreview.ui.screens.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.erapps.itunespreview.R
import com.erapps.itunespreview.data.models.Album
import com.erapps.itunespreview.ui.screens.search.searchstate.SearchDisplay
import com.erapps.itunespreview.ui.screens.search.searchstate.SearchState
import com.erapps.itunespreview.ui.screens.search.searchstate.SuggestionModel
import com.erapps.itunespreview.ui.screens.search.searchstate.rememberSearchState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    onAlbumClick: (Album?) -> Unit
) {
    val scope = rememberCoroutineScope()
    val list = viewModel.albumListState.collectAsLazyPagingItems()
    val state = rememberSearchState(searchResults = list)

    viewModel.getAlbums()
    state.searchResults = list
    getInitialSuggestions(scope, viewModel, state)

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
                NoResultsScreen()
            }
            SearchDisplay.Suggestions -> {
                SuggestionsLayout(state = state, viewModel = viewModel)
            }
            SearchDisplay.Results -> {
                AlbumsList(list = state.searchResults) { onAlbumClick(it) }
            }
            SearchDisplay.Loading -> {
                LoadingScreen()
            }
        }
        //to fill main screen when no items available
        WelcomeScreen()
    }
}

@Composable
private fun WelcomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(96.dp, 96.dp),
            tint = MaterialTheme.colors.onBackground,
            painter = painterResource(id = R.drawable.ic_music_preview_logo),
            contentDescription = "welcome"
        )
        Text(
            text = "Nothing to show, try search in the bar above.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SuggestionsLayout(
    modifier: Modifier = Modifier,
    state: SearchState,
    viewModel: SearchScreenViewModel
) {
    state.suggestions = viewModel.searchSuggestions.value
    if (state.suggestions.count() >= 7) {
        viewModel.clearSuggestions()
    }
    if (state.suggestions.size > 0) {
        Row(modifier = modifier.fillMaxWidth()) {
            Text(
                modifier = modifier.padding(8.dp),
                text = "Suggestions: ",
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
        }
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

@Composable
private fun NoResultsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Oops!, Nothing finded, try search in the bar above and press find icon again.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "or",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Check your internet connection.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

private fun getInitialSuggestions(
    scope: CoroutineScope,
    viewModel: SearchScreenViewModel,
    state: SearchState
) {
    viewModel.getSuggestions()
    scope.launch {
        viewModel.searchSuggestions.collect {
            if (it.size > 0) {
                state.suggestions = it
            }
        }
    }
}