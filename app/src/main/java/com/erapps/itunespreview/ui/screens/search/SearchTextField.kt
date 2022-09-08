package com.erapps.itunespreview.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.erapps.itunespreview.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    searchByQuery: () -> Unit,
    searching: Boolean,
    focused: Boolean,
    modifier: Modifier = Modifier
) {

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = modifier
            .then(
                Modifier
                    .height(dimensionResource(id = R.dimen.search_text_field_surface_height))
                    .padding(
                        top = dimensionResource(id = R.dimen.search_text_field_surface_padding_8),
                        bottom = dimensionResource(id = R.dimen.search_text_field_surface_padding_8),
                        start = if (!focused) dimensionResource(id = R.dimen.search_text_field_surface_padding_16) else 0.dp,
                        end = dimensionResource(id = R.dimen.search_text_field_surface_padding_16)
                    )
            ),
        color = MaterialTheme.colors.onSecondary,
        shape = RoundedCornerShape(percent = 50),
    ) {

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = modifier
            ) {

                if (query.text.isEmpty()) {
                    SearchHint(
                        modifier.padding(
                            start = dimensionResource(id = R.dimen.search_text_field_padding_start),
                            end = dimensionResource(id = R.dimen.search_text_field_padding_end)
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .onFocusChanged {
                                onSearchFocusChange(it.isFocused)
                            }
                            .focusRequester(focusRequester)
                            .padding(
                                top = dimensionResource(id = R.dimen.search_text_field_padding_end),
                                bottom = dimensionResource(id = R.dimen.search_text_field_padding_end),
                                start = dimensionResource(id = R.dimen.search_text_field_padding_16),
                                end = dimensionResource(id = R.dimen.search_text_field_padding_end)
                            ),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = MaterialTheme.colors.onBackground
                        ),
                        cursorBrush = Brush.sweepGradient(
                            colors = listOf(
                                MaterialTheme.colors.primary,
                                MaterialTheme.colors.primaryVariant
                            )
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions {
                            if (query.text.isNotEmpty()) {
                                keyboardController?.hide()
                                searchByQuery()
                            }
                        }
                    )

                    if (query.text.isNotEmpty()) {
                        IconButton(onClick = searchByQuery) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onBackground
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun SearchHint(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Text(
            color = MaterialTheme.colors.onBackground,
            text = stringResource(id = R.string.search_text_hint),
        )
    }
}