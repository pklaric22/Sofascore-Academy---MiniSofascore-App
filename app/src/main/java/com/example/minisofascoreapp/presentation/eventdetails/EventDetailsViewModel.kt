package com.example.minisofascoreapp.presentation.eventdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascoreapp.domain.model.EventDetails
import com.example.minisofascoreapp.domain.model.Incident
import com.example.minisofascoreapp.domain.repository.EventRepository
import com.example.minisofascoreapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.toLongOrNull

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val repository: EventRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(EventDetailsState())
    val state: StateFlow<EventDetailsState> = _state

    var eventId: Long? = null

    init {
        val id = savedStateHandle.get<String>("eventId")?.toLongOrNull()
            ?: error("eventId argument is missing or not a valid Long")
        eventId = id
        loadEvent(id)
    }

    fun loadEvent(id: Long) {
        viewModelScope.launch {
            _state.value = EventDetailsState(isLoading = true)
            val result = repository.getEventDetails(id)
            when (result) {
                is Result.Error -> _state.value =
                    EventDetailsState(error = result.e.message)

                is Result.Success -> _state.value = EventDetailsState(
                    details = result.data,
                )
            }
        }
    }

    fun refresh() {
        loadEvent(eventId ?: 0L)
    }
}

data class EventDetailsState(
    val isLoading: Boolean = false,
    val details: EventDetails? = null,
    val error: String? = null
)
