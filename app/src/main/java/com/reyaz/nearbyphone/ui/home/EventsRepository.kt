package com.reyaz.nearbyphone.ui.home

import androidx.paging.PagingData
import com.reyaz.nearbyphone.data.Event
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    suspend fun getAllEventsList(): Flow<PagingData<Event>>
    suspend fun getNearByEventsList(
        latitude: Double,
        longitude: Double,
        distance: String,
    ): Flow<PagingData<Event>>
}