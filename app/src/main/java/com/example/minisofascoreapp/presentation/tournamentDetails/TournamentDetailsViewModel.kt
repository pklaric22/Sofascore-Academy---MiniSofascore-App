package com.example.minisofascoreapp.presentation.tournamentDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascoreapp.domain.model.Tournament
import com.example.minisofascoreapp.domain.repository.TournamentRepository
import com.example.minisofascoreapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TournamentDetailsViewModel @Inject constructor(
    private val repository: TournamentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TournamentDetailsState())
    val state: StateFlow<TournamentDetailsState> = _state

    var tournamentId: Long? = null

    init {
        val id = savedStateHandle.get<String>("tournamentId")?.toLongOrNull()
            ?: error("eventId argument is missing or not a valid Long")
        tournamentId = id
        loadTournamentDetails(id)
    }

    fun loadTournamentDetails(id: Long) {
        viewModelScope.launch {
            _state.value = TournamentDetailsState(isLoading = true)
            val response = repository.getTournamentDetails(id)
            when (response) {
                is Result.Error -> {
                    _state.value = TournamentDetailsState(error = response.e.message)
                }

                is Result.Success -> {
                    _state.value = TournamentDetailsState(
                        details = response.data
                    )
                }
            }
        }
    }

    fun refresh() {
        loadTournamentDetails(tournamentId ?: 0L)
    }
}


data class TournamentDetailsState(
    val isLoading: Boolean = false,
    val details: Tournament? = null,
    val error: String? = null
)