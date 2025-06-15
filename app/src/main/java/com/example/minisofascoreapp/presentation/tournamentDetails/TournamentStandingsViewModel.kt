package com.example.minisofascoreapp.presentation.tournamentDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascoreapp.domain.model.Event
import com.example.minisofascoreapp.domain.model.Standings
import com.example.minisofascoreapp.domain.repository.TournamentRepository
import com.example.minisofascoreapp.utils.PaginatedSource
import com.example.minisofascoreapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TournamentStandingsViewModel @Inject constructor(
    private val repository: TournamentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TournamentStandingsState())
    val state: StateFlow<TournamentStandingsState> = _state

    var tournamentId: Long? = null

    init {
        val id = savedStateHandle.get<String>("tournamentId")?.toLongOrNull()
            ?: error("eventId argument is missing or not a valid Long")
        tournamentId = id
        loadTournamentDetails(id)
    }

    fun loadTournamentDetails(id: Long) {
        viewModelScope.launch {
            _state.value = TournamentStandingsState(isLoading = true)
            val response = repository.getStandings(id)
            when (response) {
                is Result.Error -> {
                    _state.value = TournamentStandingsState(error = response.e.message)
                }

                is Result.Success -> {
                    _state.value = TournamentStandingsState(
                        standings = response.data.firstOrNull()
                    )
                }
            }
        }
    }

    fun refresh() {
        loadTournamentDetails(tournamentId ?: 0L)
    }
}

data class TournamentStandingsState(
    val isLoading: Boolean = false,
    val standings: Standings? = null,
    val error: String? = null
)
