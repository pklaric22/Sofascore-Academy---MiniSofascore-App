package com.example.minisofascoreapp.presentation.eventlist

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascoreapp.R
import com.example.minisofascoreapp.domain.model.Event
import com.example.minisofascoreapp.domain.model.TournamentGroup
import com.example.minisofascoreapp.domain.repository.EventRepository
import com.example.minisofascoreapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

data class DateTab(val label: String, val date: LocalDate)

enum class SportType(@DrawableRes val iconRes: Int, val label: String, val slug: String) {
    FOOTBALL(R.drawable.football, "Football", "football"),
    BASKETBALL(R.drawable.icon_basketball, "Basketball", "basketball"),
    AMERICAN_FOOTBALL(R.drawable.americanfootball, "Am. Football", "american-football")
}

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _selectedSport = MutableStateFlow(SportType.FOOTBALL)
    val selectedSport: StateFlow<SportType> = _selectedSport

    private val _state = MutableStateFlow(EventListState())
    val state: StateFlow<EventListState> = _state

    val daysRange: List<DateTab>
        get() {
            val today = LocalDate.now()
            return (-3..3).map { offset ->
                val date = today.plusDays(offset.toLong())
                val label = when (offset) {
                    0 -> "Today"
                    -1 -> "Yesterday"
                    1 -> "Tomorrow"
                    else -> date.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    ) + " ${date.dayOfMonth}.${date.monthValue}."
                }
                DateTab(label, date)
            }
        }

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    init {
        getEvents()
    }

    private fun getEvents() {
        viewModelScope.launch {
            _state.value = EventListState(isLoading = true)
            val response = repository.getEventsForDate(
                sport = _selectedSport.value.slug,
                date = _selectedDate.value.toString()
            )
            when (response) {
                is Result.Error -> {
                    _state.value = EventListState(error = response.e.message)
                }

                is Result.Success -> {
                    _state.value = EventListState(
                        events = groupEventsByTournament(
                            response.data,
                            _selectedDate.value
                        )
                    )
                }
            }
        }
    }

    private fun groupEventsByTournament(
        events: List<Event>,
        selectedDate: LocalDate
    ): List<TournamentGroup> {
        return events
            .filter {
                LocalDate.parse(it.startDate.substring(0, 10)) == selectedDate
            }
            .groupBy { "${it.tournamentId}" }
            .map { (_, groupedEvents) ->
                val first = groupedEvents.first()
                TournamentGroup(
                    tournamentId = first.tournamentId,
                    countryName = first.tournamentCountryName,
                    tournamentName = first.tournamentName,
                    events = groupedEvents
                )
            }
    }

    fun onSportSelected(sport: SportType) {
        _selectedSport.value = sport
        getEvents()
    }

    fun onDateSelected(date: LocalDate) {
        if (_selectedDate.value != date) {
            _selectedDate.value = date
            getEvents()
        }
    }

    fun refresh(){
        getEvents()
    }
}

data class EventListState(
    val isLoading: Boolean = false,
    val events: List<TournamentGroup>? = null,
    val error: String? = null
)
