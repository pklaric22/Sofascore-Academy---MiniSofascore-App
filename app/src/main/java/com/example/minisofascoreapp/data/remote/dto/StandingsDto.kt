package com.example.minisofascoreapp.data.remote.dto

import android.annotation.SuppressLint
import com.example.minisofascoreapp.domain.model.Club
import com.example.minisofascoreapp.domain.model.Standings
import com.example.minisofascoreapp.domain.model.Team
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class StandingsDto(
    val id: Long,
    val tournament: TournamentDto,
    val type: String,
    val sortedStandingsRows: List<ClubDto>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ClubDto(
    val id: Long,
    val team: TeamDto,
    val points: Int?,
    val scoresFor: Int,
    val scoresAgainst: Int,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val percentage: Float?
)

fun StandingsDto.toDomain(): Standings = Standings(
    id = id,
    tournament = tournament.toDomain(),
    type = type,
    sortedStandingsRow = sortedStandingsRows.map { it.toDomain() }
)

fun ClubDto.toDomain(): Club = Club(
    id = id,
    team = team.toDomain(),
    points = points,
    scoresFor = scoresFor,
    scoresAgainst = scoresAgainst,
    played = played,
    wins = wins,
    draws = draws,
    losses = losses,
    percentage = percentage?.let { String.format("%.1f", it).toFloat() }
)

fun TeamDto.toDomain(): Team = Team(
    id = id,
    name = name,
    countryName = country.name
)