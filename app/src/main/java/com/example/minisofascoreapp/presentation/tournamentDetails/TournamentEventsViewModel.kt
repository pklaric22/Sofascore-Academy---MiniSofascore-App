package com.example.minisofascoreapp.presentation.tournamentDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.minisofascoreapp.domain.model.Event
import com.example.minisofascoreapp.domain.model.dateGroupLabel
import com.example.minisofascoreapp.domain.model.isPast
import com.example.minisofascoreapp.domain.repository.TournamentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TournamentEventsViewModel @Inject constructor(
    private val repository: TournamentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tournamentId = savedStateHandle.get<String>("tournamentId")?.toLongOrNull()
        ?: error("Missing or invalid tournamentId")

    val events: StateFlow<PagingData<Event>> = repository
        .getPagedTournamentEvents(tournamentId)
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    val eventsWithSeparators = events.map { pagingData ->
        pagingData
            .map { EventUiItem.EventItem(it) }
            .insertSeparators { before, after ->
                val beforeEvent = before?.event
                val afterEvent = after?.event

                if (beforeEvent == null && afterEvent != null) {
                    EventUiItem.Separator(afterEvent.dateGroupLabel())
                } else if (beforeEvent != null && afterEvent != null &&
                    beforeEvent.isPast() != afterEvent.isPast()
                ) {
                    EventUiItem.Separator(afterEvent.dateGroupLabel())
                } else null
            }
    }.cachedIn(viewModelScope)

}
