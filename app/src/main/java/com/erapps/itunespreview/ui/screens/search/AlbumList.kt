package com.erapps.itunespreview.ui.screens.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.erapps.itunespreview.data.models.Album

@Composable
fun AlbumsList(
    modifier: Modifier = Modifier,
    list: LazyPagingItems<Album>,
    onCardCLick: (Album) -> Unit
) {

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(list.itemCount) { i ->
            AlbumItem(album = list[i]) { onCardCLick(list[i]!!) }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlbumItem(
    modifier: Modifier = Modifier,
    album: Album?,
    onCardCLick: () -> Unit
) {
    val context = LocalContext.current
    Card(
        onClick = { onCardCLick() },
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .padding(8.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colors.surface), shape = Shapes().small)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(album?.artworkUrl100.toString())
                    .crossfade(true)
                    .build(),
                contentDescription = album?.artistName,
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .size(204.dp, 164.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = album?.collectionName.toString(),
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = album?.artistName.toString(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}