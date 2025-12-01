package com.example.myuniapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myuniapp.data.local.EventEntity
import com.example.myuniapp.repository.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventViewModel(
    private val repository: EventRepository = EventRepository.default()
) : ViewModel() {

    val events: StateFlow<List<EventEntity>> =
        repository.events.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        refreshEvents()
    }

    fun refreshEvents() {
        viewModelScope.launch {
            repository.refreshEvents()
        }
    }
}
