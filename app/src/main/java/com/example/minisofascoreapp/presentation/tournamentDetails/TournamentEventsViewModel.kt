package com.example.minisofascoreapp.presentation.tournamentDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascoreapp.domain.model.Event
import com.example.minisofascoreapp.domain.repository.TournamentRepository
import com.example.minisofascoreapp.utils.PaginatedSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TournamentEventsViewModel @Inject constructor(
    private val repository: TournamentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private lateinit var paginatedSource: PaginatedSource<Event>

    private val _state = MutableStateFlow(EventDetailsState())
    val state: StateFlow<EventDetailsState> = _state

    init {
        val tournamentId = savedStateHandle.get<String>("tournamentId")?.toLongOrNull()
            ?: error("tournamentId argument is missing or not a valid Long")
        loadEvent(tournamentId)
    }

    fun loadEvent(id: Long) {
        viewModelScope.launch {
            _state.value = EventDetailsState(isLoading = true)
            repository.getTournamentEvents(id)
                .onEach { source ->
                    paginatedSource = source
                    source.items
                        .stateIn(viewModelScope)
                        .drop(1)
                        .collect {
                            _state.value =
                                EventDetailsState(groupedEvents = it.groupBy { it.round })
                        }
                }
                .launchIn(this)
        }
    }

    fun loadNextPage() {
        paginatedSource.nextPage()
    }

    fun refresh() {
        paginatedSource.reset()
    }
}

data class EventDetailsState(
    val isLoading: Boolean = false,
    val groupedEvents: Map<Int, List<Event>> = emptyMap(),
    val error: String? = null
)
