package com.erapps.itunespreview.ui.screens.media

import android.net.Uri

data class AudioModel(
    val uri: Uri,
    val displayName: String,
    val id: Long,
    val artistName: String,
    val data: String,
    val duration: Int,
    val title: String
)
