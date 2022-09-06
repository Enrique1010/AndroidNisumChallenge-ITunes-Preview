package com.erapps.itunespreview.data.models

data class ListResponse<T>(
    val resultCount: Int,
    val results: List<T>
)