package com.example.minisofascoreapp.domain.model

data class EventDetails(
    val id: Long,
    val status: String,
    val startDate: String,
    val round: Int,
    val homeTeam: Team,
    val awayTeam: Team,
    val tournament: Tournament,
    val homeScore: Score,
    val awayScore: Score,
    val incident: List<Incident>?
)

data class Team(
    val id: Long,
    val name: String,
    val countryName: String
)

data class Tournament(
    val id: Long,
    val name: String,
    val countryName: String,
    val sportName: String,
    val round: Int?
)

data class Score(
    val total: Int?,
    val period1: Int?,
    val period2: Int?,
    val period3: Int?,
    val period4: Int?,
    val overtime: Int?
)
