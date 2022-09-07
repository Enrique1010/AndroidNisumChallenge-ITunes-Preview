package com.erapps.itunespreview.ui.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.erapps.itunespreview.data.models.Album

class SharedViewModel : ViewModel() {

    var album by mutableStateOf<Album?>(null)
        private set

    fun addAlbum(addedAlbum: Album) {
        album = addedAlbum
    }
}