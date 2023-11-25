package com.reyaz.nearbyphone.ui.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.reyaz.nearbyphone.base.BaseViewModel
import com.reyaz.nearbyphone.data.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeVM(app: Application) : BaseViewModel(app) {
    private val repository: EventsRepository by lazy { EventsRepoImpl() }

    val location = MutableStateFlow<Pair<Double, Double>?>(null)

    private val _eventsFlow = MutableStateFlow<PagingData<Event>>(PagingData.empty())
    val eventsFlow = _eventsFlow.asStateFlow()

    init {
        viewModelScope.launch {
            location.collectLatest { loc ->
                if (loc == null) {
                    _eventsFlow.emit(PagingData.empty())
                    repository.getAllEventsList().collectLatest {
                        _eventsFlow.emit(it)
                    }
                } else {
                    _eventsFlow.emit(PagingData.empty())
                    repository.getNearByEventsList(loc.first, loc.second, "100km").collectLatest {
                        _eventsFlow.emit(it)
                    }
                }
            }
        }
    }
}