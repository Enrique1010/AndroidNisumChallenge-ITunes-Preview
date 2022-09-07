package com.erapps.itunespreview.ui.screens.media

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.Player

class AudioViewModel : ViewModel() {

    var audioModel by mutableStateOf<AudioModel?>(null)
        private set

    var mediaItem: Player? by mutableStateOf(null)
        private set

    fun addSong(newSong: AudioModel) {
        audioModel = newSong
    }

    fun addMediaPlayer(addedMediaPlayer: Player) {
        mediaItem = addedMediaPlayer
    }

    fun clearSong() {
        audioModel = null
        shuttingDownMediaPlayer()
        mediaItem = null
    }

    private fun shuttingDownMediaPlayer() {
        mediaItem?.stop()
        mediaItem?.release()
    }
}
