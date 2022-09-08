package com.erapps.itunespreview.ui.screens.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.erapps.itunespreview.R
import com.erapps.itunespreview.data.models.Album
import com.erapps.itunespreview.ui.utils.Constants.GRID_AMOUNT_OF_CELLS

@Composable
fun AlbumsList(
    modifier: Modifier = Modifier,
    list: LazyPagingItems<Album>,
    onCardCLick: (Album) -> Unit
) {

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(GRID_AMOUNT_OF_CELLS),
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
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.rounder_shape_album_item)),
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_card_album_item))
            .border(
                BorderStroke(
                    dimensionResource(id = R.dimen.border_stroke_card_album_item),
                    MaterialTheme.colors.surface
                ),
                shape = Shapes().small
            )
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
                modifier = Modifier.fillMaxWidth(),
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