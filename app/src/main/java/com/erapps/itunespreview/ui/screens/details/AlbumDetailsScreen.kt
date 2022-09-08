package com.erapps.itunespreview.ui.screens.details

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.erapps.itunespreview.R
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

    val scaffoldState = rememberBottomSheetScaffoldState()
    val album = sharedViewModel.album

    BottomSheetScaffold(
        sheetContent = {
            Box(Modifier.defaultMinSize(minHeight = dimensionResource(id = R.dimen.default_min_size))) {
                if (audioViewModel.audioModel != null) {
                    BottomMediaContent()
                }
            }
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = dimensionResource(id = R.dimen.details_screen_bottom_sheet_peek_height),
        sheetShape = RectangleShape
    ) {
        DetailsContent(album = album!!, scaffoldState = scaffoldState) {
            audioViewModel.clearSong()
            onPopup()
        }
    }
    BackHandler {
        audioViewModel.clearSong()
        onPopup()
    }
}

//to clear song state on back pressed not only on back press button or gesture
@Composable
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    val currentOnBack by rememberUpdatedState(onBack)
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }
    SideEffect { backCallback.isEnabled = enabled }
    val backDispatcher =
        checkNotNull(LocalOnBackPressedDispatcherOwner.current) {}.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    album: Album,
    scaffoldState: BottomSheetScaffoldState,
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
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.details_content_spacer)))
            Column(
                modifier = modifier.padding(dimensionResource(id = R.dimen.details_content_column_padding))
            ) {
                Text(
                    text = album.collectionName,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.details_content_text_size).value.sp
                )
                Text(
                    text = album.artistName,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.details_content_spacer)))
            SongsList(albumId = album.collectionId.toLong(), scaffoldState = scaffoldState)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    viewModel: AlbumDetailsViewModel = hiltViewModel(),
    scaffoldState: BottomSheetScaffoldState,
    albumId: Long
) {
    val list = viewModel.songListState.collectAsState()

    //remove first item because is not a song
    val mutableList: MutableList<Song> = mutableListOf()
    mutableList.addAll(list.value)
    viewModel.getSongByAlbumId(albumId)
    if (mutableList.isNotEmpty()) {
        mutableList.removeFirst()
    }

    if (mutableList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.no_music_available_text),
                fontSize = dimensionResource(id = R.dimen.song_list_font_size).value.sp
            )
        }
        return
    }
    LazyColumn(
        modifier = modifier.padding(
            end = dimensionResource(id = R.dimen.song_list_lazy_column_padding),
            start = dimensionResource(id = R.dimen.song_list_lazy_column_padding)
        ),
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
    scaffoldState: BottomSheetScaffoldState,
) {
    val scope = rememberCoroutineScope()

    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.song_list_item_spacer)))
    SongBox(
        collectionName = song.trackName,
        imageUrl = song.artworkUrl100,
        artistName = song.artistName
    ) {
        if (viewModel.audioModel != null) {
            viewModel.clearSong()
        }
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
            scaffoldState.bottomSheetState.expand()
        }
    }
    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.song_list_item_spacer)))
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
        modifier = modifier
            .clickable { onPlayClicked() }
            .background(color = MaterialTheme.colors.onSecondary),
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
                    .size(
                        dimensionResource(id = R.dimen.song_box_image_size),
                        dimensionResource(id = R.dimen.song_box_image_size)
                    )
                    .padding(
                        end = dimensionResource(id = R.dimen.song_box_image_padding),
                        start = dimensionResource(id = R.dimen.song_box_image_padding)
                    ),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = modifier.fillMaxWidth()
            ) {
                MarqueeText(
                    modifier = modifier.fillMaxWidth(),
                    text = collectionName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = artistName,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.song_box_text_size).value.sp
                )
            }
        }
    }
}