package com.erapps.itunespreview.ui.screens.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.erapps.itunespreview.data.models.Album
import com.erapps.itunespreview.data.models.Song
import com.erapps.itunespreview.ui.screens.media.AudioModel
import com.erapps.itunespreview.ui.screens.media.AudioViewModel
import com.erapps.itunespreview.ui.screens.media.BottomMediaContent
import com.erapps.itunespreview.ui.shared.MarqueeText
import com.erapps.itunespreview.ui.shared.SharedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlbumDetailsScreen(
    sharedViewModel: SharedViewModel,
    audioViewModel: AudioViewModel = viewModel(),
    onPopup: () -> Unit
) {
    val scaffoldState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val album = sharedViewModel.album

    ModalBottomSheetLayout(
        sheetContent = {
            Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                if (audioViewModel.audioModel != null) {
                    BottomMediaContent()
                }
            }
        },
        sheetState = scaffoldState,
        scrimColor = Color.Transparent,
        sheetShape = RectangleShape
    ) {
        DetailsContent(album = album!!, scaffoldState = scaffoldState) { onPopup() }
    }

    //to handle clear media player when sheet is hide and initialize when is expanded
    LaunchedEffect(scaffoldState.currentValue) {
        when (scaffoldState.currentValue) {
            ModalBottomSheetValue.Hidden -> {
                if (audioViewModel.audioModel != null) {
                    audioViewModel.clearSong()
                }
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    album: Album,
    scaffoldState: ModalBottomSheetState,
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
            SongsList(albumId = album.collectionId.toLong(), scaffoldState = scaffoldState)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    viewModel: AlbumDetailsViewModel = hiltViewModel(),
    scaffoldState: ModalBottomSheetState,
    albumId: Long
) {
    val list = viewModel.songListState.collectAsState()

    //remove first item because is not a song
    val mutableList: MutableList<Song> = mutableListOf()
    mutableList.addAll(list.value)
    viewModel.getSongByAlbumId(albumId)
    if (mutableList.isNotEmpty()) { mutableList.removeFirst() }

    if (mutableList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Ups!, no music to show.", fontSize = 16.sp)
        }
        return
    }
    LazyColumn(
        modifier = modifier.padding(end = 8.dp, start = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(mutableList as List<Song>) { song ->
            SongListItem(song = song, scaffoldState = scaffoldState)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SongListItem(
    modifier: Modifier = Modifier,
    song: Song,
    viewModel: AudioViewModel = viewModel(),
    scaffoldState: ModalBottomSheetState,
) {
    val scope = rememberCoroutineScope()

    Divider()
    SongBox(
        collectionName = song.trackName,
        imageUrl = song.artworkUrl100,
        artistName = song.artistName
    ) {
        viewModel.addSong(
            AudioModel(
                url = song.previewUrl,
                displayName = song.trackName,
                artist = song.artistName,
                currentSongId = song.trackId,
                albumImageUrl = song.artworkUrl100
            )
        )
        scope.launch {
            scaffoldState.show()
        }
    }
    Divider(modifier = modifier.padding(4.dp))
}

@Composable
fun SongBox(
    modifier: Modifier = Modifier,
    collectionName: String,
    artistName: String,
    imageUrl: String,
    onPlayClicked: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.clickable { onPlayClicked() },
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .size(24.dp, 24.dp)
                    .fillMaxWidth(0.2f)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = modifier.fillMaxWidth(0.8f)
            ) {
                MarqueeText(
                    modifier = modifier.fillMaxWidth(0.8f),
                    text = collectionName
                )
                Text(text = artistName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}