package com.reyaz.nearbyphone.ui.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.reyaz.nearbyphone.data.Event
import com.reyaz.nearbyphone.network.RetrofitProvider
import kotlinx.coroutines.flow.Flow

class EventsRepoImpl : EventsRepository {
    private val apiService by lazy { RetrofitProvider.getService() }

    override suspend fun getAllEventsList(): Flow<PagingData<Event>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                EventsPagingDataSource(apiService)
            }
        ).flow
    }

    override suspend fun getNearByEventsList(
        latitude: Double,
        longitude: Double,
        distance: String,
    ): Flow<PagingData<Event>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NearbyEventsPagingSource(
                    apiService,
                    latitude,
                    longitude,
                    distance
                )
            }
        ).flow

    }
}