package com.erapps.itunespreview.ui.screens.search.searchstate

import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.paging.compose.LazyPagingItems
import com.erapps.itunespreview.data.models.Album

@Stable
class SearchState(
    query: TextFieldValue,
    focused: Boolean,
    searching: Boolean,
    suggestions: MutableList<SuggestionModel>,
    searchResults: LazyPagingItems<Album>
) {
    var query by mutableStateOf(query)
    var focused by mutableStateOf(focused)
    var searching by mutableStateOf(searching)
    var suggestions by mutableStateOf(suggestions)
    var searchResults by mutableStateOf(searchResults)

    val searchDisplay: SearchDisplay
        get() = when {
            !focused && query.text.isEmpty() -> SearchDisplay.InitialResults
            focused && query.text.isEmpty() -> SearchDisplay.Suggestions
            focused && searchResults.itemCount == 0 -> SearchDisplay.NoResults
            searchResults.itemCount > 0 -> SearchDisplay.Results
            searching -> SearchDisplay.Loading
            else -> SearchDisplay.InitialResults
        }

    override fun toString(): String {
        return "State query: $query, focused: $focused, searching: $searching " +
                "suggestions: ${suggestions.size}, " +
                "searchResults: ${searchResults}, " +
                " searchDisplay: $searchDisplay"
    }
}

@Composable
fun rememberSearchState(
    query: TextFieldValue = TextFieldValue(""),
    focused: Boolean = false,
    searching: Boolean = false,
    suggestions: MutableList<SuggestionModel> = mutableListOf(),
    searchResults: LazyPagingItems<Album>
): SearchState {
    return remember {
        SearchState(
            query = query,
            focused = focused,
            searching = searching,
            suggestions = suggestions,
            searchResults = searchResults
        )
    }
}