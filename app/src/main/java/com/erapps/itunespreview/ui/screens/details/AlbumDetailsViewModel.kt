package com.erapps.itunespreview.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erapps.itunespreview.data.models.Song
import com.erapps.itunespreview.data.source.remote.song.ISongListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    private val songListRepository: ISongListRepository
) : ViewModel() {

    private val _songListState = MutableStateFlow<List<Song>>(emptyList())
    val songListState = _songListState.asStateFlow()

    fun getSongByAlbumId(albumId: Long) = viewModelScope.launch {
        songListRepository.getSongListByAlbumId(albumId).collect { songs ->
            _songListState.update { songs }
        }
    }

}