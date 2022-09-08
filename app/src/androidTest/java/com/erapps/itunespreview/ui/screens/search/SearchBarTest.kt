package com.erapps.itunespreview.ui.screens.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.input.TextFieldValue
import androidx.test.filters.LargeTest
import com.erapps.itunespreview.ui.screens.utils.TestTags.ICON_BUTTON_TAG
import com.erapps.itunespreview.ui.screens.utils.TestTags.SEARCH_BAR_TAG
import com.erapps.itunespreview.ui.screens.utils.TestTags.TRAILING_ICON_BUTTON_TAG
import com.erapps.itunespreview.ui.theme.ITunesPreviewTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalAnimationApi::class)
@LargeTest
class SearchBarTest {
    @get:Rule val composeRule = createComposeRule()

    private val searchQuery = mutableStateOf(TextFieldValue(""))
    private val focused = mutableStateOf(false)
    private val searching = mutableStateOf(false)

    @Before
    fun setUp() {
        composeRule.setContent {
            ITunesPreviewTheme {
                SearchBar(
                    query = searchQuery.value,
                    onQueryChange = { searchQuery.value = it },
                    onSearchFocusChange = { focused.value = it },
                    searchByQuery = {  },
                    onBack = {  },
                    searching = searching.value,
                    focused = focused.value
                )
            }
        }
    }

    @Test
    fun focusIsClearedWhenIconBackButtonIsPressed() {
        // when
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performTextInput("Michael")
        // asserting if focused when text is entered
        composeRule.onNodeWithTag(SEARCH_BAR_TAG)

        // clicking on close icon to remove focus
        composeRule.onNodeWithTag(ICON_BUTTON_TAG).performClick()

        // asserting focus is lost
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).assertIsNotFocused()
    }

    @Test
    fun fieldTextIsEqualToQuery() {
        val query = "Hello"
        // when
        // asserting if text is correct
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performTextInput(query)
        assertThat(searchQuery.value.text).isEqualTo(query)
    }

    @Test
    fun focusIsKeptWhenImeActionIsPressed() {
        val query = "Hello"
        // when
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performTextInput(query)
        // asserting focus is present
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).assertIsFocused()

        // clicking on keyboard ime action
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performImeAction()

        // asserting focus is lost
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).assertIsFocused()
    }

    @Test
    fun trailingIconButtonIsDisplayedWhenTextIsNotEmpty() {
        val query = "Hello"
        // when
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performTextInput(query)
        // asserting focus is present
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).assertIsFocused()

        composeRule.onNodeWithTag(TRAILING_ICON_BUTTON_TAG).assertIsDisplayed()
    }

    @Test
    fun trailingIconButtonIsHiddenWhenTextIsEmpty() {
        val query = ""
        // when
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performTextInput(query)
        // asserting focus is present
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).assertIsFocused()

        composeRule.onNodeWithTag(TRAILING_ICON_BUTTON_TAG).assertDoesNotExist()
    }
}