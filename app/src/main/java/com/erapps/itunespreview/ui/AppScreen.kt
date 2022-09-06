package com.erapps.itunespreview.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.erapps.itunespreview.ui.navigation.MainNavigationGraph
import com.erapps.itunespreview.ui.theme.ITunesPreviewTheme

@Composable
fun AppScreen() {
    ITunesPreviewTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            MainNavigationGraph()
        }
    }
}

/*AlbumDetailsScreen(
                album = AlbumDetailsItem(
                    "",
                    "Thriller",
                    "MJ",
                    0,
                    ""
                )
            )*/