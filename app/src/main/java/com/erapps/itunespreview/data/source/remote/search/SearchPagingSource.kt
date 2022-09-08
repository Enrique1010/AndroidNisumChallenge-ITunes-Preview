package com.erapps.itunespreview.data.source.remote.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.erapps.itunespreview.data.api.NetworkResponse
import com.erapps.itunespreview.data.api.service.ITunesApiService
import com.erapps.itunespreview.data.models.Album
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource(
    private val iTunesApiService: ITunesApiService,
    private val term: String?
) : PagingSource<Int, Album>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        return try {
            val response = iTunesApiService.getAlbumsByTerm(
                limit = params.loadSize,
                term = term!!
            )

            val albums = response.let { list ->
                when (list) {
                    is NetworkResponse.ApiError -> emptyList()
                    is NetworkResponse.NetworkError -> emptyList()
                    is NetworkResponse.Success -> list.body?.results
                    is NetworkResponse.UnknownError -> emptyList()
                }
            }

            LoadResult.Page(
                data = albums!!,
                prevKey = null,
                nextKey = null //api without page number
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Album>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}