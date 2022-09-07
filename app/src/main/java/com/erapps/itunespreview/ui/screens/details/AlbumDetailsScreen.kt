package com.erapps.itunespreview.ui.screens.details

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.erapps.itunespreview.data.models.Album
import com.erapps.itunespreview.data.models.Song
import com.erapps.itunespreview.ui.shared.MarqueeText
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
                collectionName = album!!.collectionName.ifEmpty { "klk" },
                imageUrl = album.artworkUrl100,
                artistName = album.artistName,
                audioDataSource = ""
            )
        },
        scaffoldState = scaffoldState,
        sheetGesturesEnabled = true,
        sheetShape = RectangleShape
    ) {
        DetailsContent(album = album!!) { onPopup() }
    }
}

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    album: Album,
    onPopup: () -> Unit
) {
    val context = LocalContext.current

    Surface {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                modifier = modifier,
                onClick = { onPopup() }
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }

        Column {
            Box(
                modifier = modifier
                    .fillMaxWidth(0.6f)
                    .fillMaxHeight(0.30f)
                    .align(Alignment.CenterHorizontally),
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
            Spacer(modifier = modifier.height(4.dp))
            SongsList(albumId = album.collectionId.toLong())
        }
    }
}

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    viewModel: AlbumDetailsViewModel = hiltViewModel(),
    albumId: Long
) {
    val list = viewModel.songListState.collectAsState()
    val mutableList: MutableList<Song> = mutableListOf()
    mutableList.addAll(list.value)
    viewModel.getSongByAlbumId(albumId)
    if (mutableList.isNotEmpty()) {
        mutableList.removeFirst()
    }

    LazyColumn(
        modifier = modifier.padding(end = 8.dp, start = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(mutableList as List<Song>) { song ->
            SongListItem(song = song)
        }
    }
}

@Composable
fun SongListItem(song: Song) {
    Divider()
    SongPreview(
        collectionName = song.trackName,
        imageUrl = song.artworkUrl100,
        artistName = song.artistName,
        audioDataSource = song.previewUrl
    )
    Divider()
}

@Composable
fun SongPreview(
    modifier: Modifier = Modifier,
    collectionName: String,
    artistName: String,
    imageUrl: String,
    audioDataSource: String
) {
    val context = LocalContext.current
    //setting media player
    val mediaPlayer = MediaPlayer()
    mediaPlayer.setAudioAttributes(
        AudioAttributes
            .Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
    )

    val isPlaying by remember { mutableStateOf(mediaPlayer.isPlaying) }

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
            Column {
                MarqueeText(
                    modifier = modifier.fillMaxWidth(0.8f),
                    text = collectionName
                )
                Text(text = artistName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            IconButton(
                onClick = {
                    if (isPlaying) run {


                    } else run {

                    }
                }
            ) {
                Icon(
                    imageVector = if (!isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = null
                )
            }
        }
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