package com.erapps.itunespreview.ui.screens.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.filters.MediumTest
import com.erapps.itunespreview.ui.screens.utils.TestTags.CIRCULAR_PROGRESS_INDICATOR
import com.erapps.itunespreview.ui.theme.ITunesPreviewTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
class ProgressIndicatorTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setUp() {
        composeRule.setContent {
            ITunesPreviewTheme {
                LoadingScreen()
            }
        }
    }

    @Test
    fun progressIndicatorIsDisplayed() {
        composeRule.onNodeWithTag(CIRCULAR_PROGRESS_INDICATOR).assertIsDisplayed()
    }

}