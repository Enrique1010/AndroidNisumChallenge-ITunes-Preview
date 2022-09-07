package com.erapps.itunespreview.data.api.service

import com.erapps.itunespreview.data.api.NetworkResponse
import com.erapps.itunespreview.data.models.Album
import com.erapps.itunespreview.data.models.ListResponse
import com.erapps.itunespreview.data.models.Song
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("search")
    suspend fun getAlbumsByTerm(
        @Query("term") term: String,
        @Query("mediaType") mediaType: String = "music",
        @Query("entity") entity: String = "album",
        @Query("limit") limit: Int = 20
    ): NetworkResponse<ListResponse<Album>, *>

    @GET("lookup")
    suspend fun getSongsByAlbumId(
       @Query("id") albumId: Long,
       @Query("entity") entity: String = "song"
    ): NetworkResponse<ListResponse<Song>, *>
}