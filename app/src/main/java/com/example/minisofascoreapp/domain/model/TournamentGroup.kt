package com.example.minisofascoreapp.domain.model

data class TournamentGroup(
    val tournamentId: Long,
    val countryName: String,
    val tournamentName: String,
    val events: List<Event>
)
