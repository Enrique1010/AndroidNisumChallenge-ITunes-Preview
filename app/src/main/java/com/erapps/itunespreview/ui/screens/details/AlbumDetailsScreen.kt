package com.erapps.itunespreview.ui.screens.details

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.erapps.itunespreview.R
import com.erapps.itunespreview.data.models.Album
import com.erapps.itunespreview.data.models.Song
import com.erapps.itunespreview.ui.shared.SharedViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlbumDetailsScreen(
    sharedViewModel: SharedViewModel,
    onPopup: () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    val album = sharedViewModel.album

    BottomSheetScaffold(
        sheetContent = {
            SongPreview(
                collectionName = album!!.collectionName,
                imageUrl = album.artworkUrl100,
                audioDataSource = ""
            )
        },
        scaffoldState = scaffoldState,
        sheetGesturesEnabled = false,
        sheetShape = RectangleShape
    ) {
        DetailsContent(album = album!!)
    }
}

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    album: Album
) {
    val context = LocalContext.current

    Surface {
        Column {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.30f),
                contentAlignment = Alignment.TopCenter
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(album.artworkUrl100)
                        .crossfade(true)
                        .build(),
                    contentDescription = album.artistName,
                    alignment = Alignment.TopCenter,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                /*Icon(
                    modifier = modifier.size(200.dp, 200.dp),
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )*/
            }
            Spacer(modifier = modifier.height(4.dp))
            Column(
                modifier = modifier.padding(16.dp)
            ) {
                Text(
                    text = album.collectionName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = album.artistName,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = modifier.height(4.dp))
        LazyColumn {
            /*items(album.albumId.toInt()) { item ->
                Text(text = "Song $item")
            }*/
        }
    }
}

@Composable
fun songsList(
    viewModel: AlbumDetailsViewModel = hiltViewModel(),
    albumId: Long
) {
    val list = viewModel.songListState.collectAsState()
    viewModel.getSongByAlbumId(albumId)

    LazyColumn {
        items(list.value){ item ->

        }
    }
}

@Composable
fun SongListItem(song: Song) {

}

@Composable
fun SongPreview(
    modifier: Modifier = Modifier,
    collectionName: String,
    imageUrl: String,
    audioDataSource: String
) {
    val context = LocalContext.current
    val progress by remember { mutableStateOf(.0f) }
    //setting media player
    val mediaPlayer = MediaPlayer()
    mediaPlayer.setAudioAttributes(
        AudioAttributes
            .Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
    )

    val isPlaying by remember { mutableStateOf(mediaPlayer.isPlaying) }
    mediaPlayer.apply {
//        setDataSource(LocalContext.current, Uri.parse(audioDataSource))
//        prepare()
    }

    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                alignment = Alignment.TopCenter,
                modifier = Modifier.size(24.dp, 24.dp),
                contentScale = ContentScale.Crop
            )
            /*Image(
                modifier = modifier.padding(start = 8.dp),
                painter = painterResource(id = R.drawable.ic_music_preview_logo),
                contentDescription = null
            )*/
            Text(text = collectionName)
            IconButton(
                onClick = {
                    if (isPlaying) run {
                        mediaPlayer.apply {
                            stop()
                            reset()
                            release()
                        }

                    } else run {
                        mediaPlayer.apply {
                            setDataSource(context, Uri.parse(audioDataSource))
                            prepare()
                            start()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = if (!isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = null
                )
            }
        }
        LinearProgressIndicator(
            modifier = modifier.fillMaxWidth(),
            progress = progress
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AlbumDetailsScreenPreview() {
    /*AlbumDetailsScreen(
        album = AlbumDetailsItem(
            "",
            "Thriller",
            "MJ",
            0
        )
    ){}*/
}