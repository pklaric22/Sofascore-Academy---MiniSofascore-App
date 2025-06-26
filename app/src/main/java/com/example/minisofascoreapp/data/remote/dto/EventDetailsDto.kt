package com.example.minisofascoreapp.data.remote.dto

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class EventDetailsDto(
    val id: Long,
    val slug: String,
    val status: String,
    val startDate: String,
    val round: Int,
    val homeTeam: TeamDto,
    val awayTeam: TeamDto,
    val tournament: TournamentDto,
    val homeScore: ScoreDto,
    val awayScore: ScoreDto
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class IncidentDto(
    val id: Long,
    val text: String?,
    val player: PlayerDto?,
    val teamSide: String?,
    val color: String?,
    val time: Int,
    val type: String,
    val scoringTeam: String?,
    val goalType: String?,
    val homeScore: Int?,
    val awayScore: Int?
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PlayerDto(
    val id: Long,
    val name: String,
    val slug: String,
    val position: String,
    val country: CountryDto?
)


