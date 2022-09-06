package com.erapps.itunespreview.ui.screens.details

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erapps.itunespreview.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlbumDetailsScreen(
    album: AlbumDetailsItem,
    onPopup: () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        sheetContent = {
            SongPreview(
                collectionName = album.albumName,
                audioDataSource =
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/99/3a/92/993a9275-10d0-1be8-58d5-17607497e48d/mzaf_10635522880453166317.plus.aac.p.m4a"
            )
        },
        scaffoldState = scaffoldState,
        sheetGesturesEnabled = false,
        sheetShape = RectangleShape
    ) {
        DetailsContent(album = album)
    }
}

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    album: AlbumDetailsItem
) {
    Surface {
        Column {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.30f),
                contentAlignment = Alignment.TopCenter
            ) {
                Icon(
                    modifier = modifier.size(200.dp, 200.dp),
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            }
            Spacer(modifier = modifier.height(4.dp))
            Column(
                modifier = modifier.padding(16.dp)
            ) {
                Text(
                    text = album.albumName,
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
fun SongPreview(
    modifier: Modifier = Modifier,
    collectionName: String,
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
        setDataSource(LocalContext.current, Uri.parse(audioDataSource))
        prepare()
    }

    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = modifier.padding(start = 8.dp),
                painter = painterResource(id = R.drawable.ic_music_preview_logo),
                contentDescription = null
            )
            Text(text = "preview of $collectionName")
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
    AlbumDetailsScreen(
        album = AlbumDetailsItem(
            "",
            "Thriller",
            "MJ",
            0
        )
    ){}
}