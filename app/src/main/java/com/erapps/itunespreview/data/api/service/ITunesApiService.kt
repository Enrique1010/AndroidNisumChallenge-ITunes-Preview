package com.erapps.itunespreview.data.api.service

import com.erapps.itunespreview.data.api.NetworkResponse
import com.erapps.itunespreview.data.models.ListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("search")
    suspend fun getAlbumsByTerm(
        @Query("term") term: String,
        @Query("mediaType") mediaType: String = "music",
        @Query("limit") limit: Int
    ): NetworkResponse<ListResponse, *>
}