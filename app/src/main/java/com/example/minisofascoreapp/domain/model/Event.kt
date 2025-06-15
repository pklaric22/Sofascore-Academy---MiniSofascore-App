package com.example.minisofascoreapp.domain.model

data class Event(
    val id: Long,
    val tournamentName: String,
    val homeTeamName: String,
    val awayTeamName: String,
    val status: String,
    val startDate: String,
    val homeScore: Int?,
    val awayScore: Int?,
    val countryName: String,
    val sportName: String,
    val homeTeamId: Long,
    val awayTeamId: Long,
    val tournamentId: Long,
    val tournamentCountryName: String,
    val round: Int
)

