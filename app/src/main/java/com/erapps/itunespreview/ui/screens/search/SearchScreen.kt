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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.erapps.itunespreview.R
import com.erapps.itunespreview.data.models.Album
import com.erapps.itunespreview.ui.screens.search.searchstate.SearchDisplay
import com.erapps.itunespreview.ui.screens.search.searchstate.SearchState
import com.erapps.itunespreview.ui.screens.search.searchstate.SuggestionModel
import com.erapps.itunespreview.ui.screens.search.searchstate.rememberSearchState
import com.erapps.itunespreview.ui.screens.utils.TestTags.CIRCULAR_PROGRESS_INDICATOR
import com.erapps.itunespreview.ui.utils.Constants.SUGGESTION_LIST_LIMIT
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
            .padding(dimensionResource(id = R.dimen.welcome_screen_padding)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(
                dimensionResource(id = R.dimen.welcome_icon_size),
                dimensionResource(id = R.dimen.welcome_icon_size)
            ),
            tint = MaterialTheme.colors.onBackground,
            painter = painterResource(id = R.drawable.ic_music_preview_logo),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.welcome_text),
            fontSize = dimensionResource(id = R.dimen.no_results_screen_font_size).value.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().testTag(CIRCULAR_PROGRESS_INDICATOR),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SuggestionsLayout(
    modifier: Modifier = Modifier,
    state: SearchState,
    viewModel: SearchScreenViewModel
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    state.suggestions = viewModel.searchSuggestions.value
    if (state.suggestions.count() >= SUGGESTION_LIST_LIMIT) {
        viewModel.clearSuggestions()
    }
    if (state.suggestions.size > 0) {
        Row(modifier = modifier.fillMaxWidth()) {
            Text(
                modifier = modifier.padding(dimensionResource(id = R.dimen.suggestion_layout_text_padding)),
                text = stringResource(R.string.suggestion_label),
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
                    keyboardController?.hide()
                    viewModel.getAlbums()
                }
            ) {
                Icon(imageVector = Icons.Default.History, contentDescription = null)
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.suggestion_layout_item_spacer)))
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
            .padding(dimensionResource(id = R.dimen.no_results_screen_column_padding)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_results_error_message),
            fontSize = dimensionResource(id = R.dimen.no_results_screen_font_size).value.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.or_text),
            textAlign = TextAlign.Center,
            fontSize = dimensionResource(id = R.dimen.no_results_screen_font_size).value.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.check_internet_text),
            fontSize = dimensionResource(id = R.dimen.no_results_screen_font_size).value.sp,
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