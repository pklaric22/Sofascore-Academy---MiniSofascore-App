package com.example.minisofascoreapp.domain.model

data class Standings(
    val id: Long,
    val tournament: Tournament,
    val type: String,
    val sortedStandingsRow: List<Club>
)

data class Club(
    val id: Long,
    val team: Team,
    val points: Int?,
    val scoresFor: Int,
    val scoresAgainst: Int,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val percentage: Float?
)

