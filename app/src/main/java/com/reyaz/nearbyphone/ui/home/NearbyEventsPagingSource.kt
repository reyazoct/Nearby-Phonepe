package com.reyaz.nearbyphone.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.reyaz.nearbyphone.data.Event
import com.reyaz.nearbyphone.network.ApiService

class NearbyEventsPagingSource(
    private val service: ApiService,
    private val latitude: Double,
    private val longitude: Double,
    private val distance: String,
) : PagingSource<Int, Event>() {

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {

        val position = params.key ?: 1
        return try {
            val response = service.getVenuesByLocation(
                10,
                position,
                latitude,
                longitude,
                distance
            )
            val data = response.body()?.venues
            if (response.isSuccessful && data != null) {
                val nextKey = if (data.isEmpty()) {
                    null
                } else {
                    position + 1
                }

                LoadResult.Page(
                    data = data,
                    prevKey = if (position == 0) null else position - 1,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(Throwable(response.message()))
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}