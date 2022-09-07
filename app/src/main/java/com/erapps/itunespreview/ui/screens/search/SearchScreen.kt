package com.erapps.itunespreview.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.erapps.itunespreview.data.models.Album

@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    onAlbumClick: (Album?) -> Unit
) {
    val list = viewModel.albumListState.collectAsLazyPagingItems()
    val text by viewModel.termQuery

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchBar(text = text) { viewModel.updateSearchText(it) }
        viewModel.getAlbums()
        AlbumsList(modifier = Modifier.padding(start = 16.dp, end = 16.dp), list = list) {
            onAlbumClick(it)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = text,
        onValueChange = { onTextChange(it) },
        placeholder = { SearchBarText(modifier) },
        textStyle = TextStyle(fontSize = MaterialTheme.typography.subtitle1.fontSize),
        singleLine = true,
        trailingIcon = {
            if (text.isNotEmpty()){
                SearchBarIcon(modifier) {
                    onTextChange("")
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            cursorColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.medium)
        )
    )
}

@Composable
private fun SearchBarIcon(
    modifier: Modifier,
    onClickIcon: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { onClickIcon() }
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Close Icon",
            tint = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
private fun SearchBarText(modifier: Modifier) {
    Text(
        modifier = modifier.alpha(ContentAlpha.medium),
        text = "Search...",
        color = MaterialTheme.colors.onBackground
    )
}