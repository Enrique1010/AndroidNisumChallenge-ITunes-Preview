package com.erapps.itunespreview.ui.screens.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.erapps.itunespreview.ui.shared.MarqueeText
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerControlView

@Composable
fun BottomMediaContent(
    modifier: Modifier = Modifier,
    viewModel: AudioViewModel = viewModel()
) {
    val song = viewModel.audioModel
    //media related
    var currentPosition by rememberSaveable { mutableStateOf(0L) }
    val player = song?.let { generatePlayer(viewModel ,it.url) }
    val playerView = rememberPlayerViewWithLifecycle { currentPosition = it }

    Column {
        Row(
            modifier = modifier.fillMaxWidth()
                .background(MaterialTheme.colors.background),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song?.albumImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .size(24.dp, 24.dp)
                    .padding(start = 8.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = modifier.padding(start = 8.dp)
            ) {
                song?.displayName?.let {
                    MarqueeText(
                        modifier = modifier.fillMaxWidth(0.8f),
                        text = it
                    )
                }
                song?.artist?.let {
                    Text(text = it, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
        PlayerContainer(
                playerView = playerView,
        player = player!!,
        currentPosition = currentPosition,
        modifier = modifier
        )
    }
}

@Composable
private fun PlayerContainer(
    playerView: PlayerControlView,
    player: ExoPlayer,
    currentPosition: Long,
    modifier: Modifier
) {
    AndroidView(modifier = modifier, factory = { playerView }) { playerAndroidView ->
        playerAndroidView.player = player
        playerAndroidView.showTimeoutMs = 0
        playerAndroidView.player?.seekTo(currentPosition)
    }
}

@Composable
private fun rememberPlayerLifecycleObserver(player: PlayerControlView): LifecycleEventObserver =
    remember(player) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_DESTROY -> player.player?.release()
                else -> {}
            }
        }
    }

@Composable
private fun rememberPlayerViewWithLifecycle(onDisposeCalled: (Long) -> Unit): PlayerControlView {
    val context = LocalContext.current
    val playerView = remember {
        PlayerControlView(context).apply {
            setShowNextButton(false)
            setShowPreviousButton(false)
        }
    }
    val lifecycleObserver = rememberPlayerLifecycleObserver(playerView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            val position = playerView.player?.currentPosition ?: 0
            onDisposeCalled(position)
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return playerView
}

@Composable
private fun generatePlayer(viewModel: AudioViewModel, uri: String): ExoPlayer {
    val player = ExoPlayer.Builder(LocalContext.current).build()
    val mediaItem = MediaItem.fromUri(uri)
    player.setMediaItem(mediaItem)
    player.prepare()
    viewModel.addMediaPlayer(player)
    return player
}