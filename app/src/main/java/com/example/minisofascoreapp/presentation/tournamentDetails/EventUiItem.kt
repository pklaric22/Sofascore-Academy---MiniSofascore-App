package com.example.minisofascoreapp.presentation.tournamentDetails

import com.example.minisofascoreapp.domain.model.Event

sealed class EventUiItem {
    data class EventItem(val event: Event) : EventUiItem()
    data class Separator(val label: String) : EventUiItem()
}
