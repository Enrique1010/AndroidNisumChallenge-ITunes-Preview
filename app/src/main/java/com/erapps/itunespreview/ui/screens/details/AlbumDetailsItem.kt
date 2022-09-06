package com.erapps.itunespreview.ui.screens.details

data class AlbumDetailsItem(
    val imageURL: String,
    val albumName: String,
    val artistName: String,
    val songsCount: Int,
    val previewSoundUrl: String
)