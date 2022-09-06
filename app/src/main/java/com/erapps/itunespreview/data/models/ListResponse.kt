package com.erapps.itunespreview.data.models

data class ListResponse(
    val resultCount: Int,
    val results: List<Album>
)