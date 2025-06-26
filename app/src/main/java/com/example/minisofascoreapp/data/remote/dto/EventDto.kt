package com.example.minisofascoreapp.data.remote.dto

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class EventDto(
    val id: Long,
    val slug: String,
    val tournament: TournamentDto,
    val homeTeam: TeamDto,
    val awayTeam: TeamDto,
    val status: String,
    val startDate: String,
    val homeScore: ScoreDto,
    val awayScore: ScoreDto,
    val winnerCode: String? = null,
    val round: Int? = null
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TournamentDto(
    val id: Long,
    val name: String,
    val slug: String,
    val sport: SportDto,
    val country: CountryDto
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TeamDto(
    val id: Long,
    val name: String,
    val country: CountryDto
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class CountryDto(
    val id: Long,
    val name: String
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class SportDto(
    val id: Long,
    val name: String,
    val slug: String
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ScoreDto(
    val total: Int? = null,
    val period1: Int? = null,
    val period2: Int? = null,
    val period3: Int? = null,
    val period4: Int? = null,
    val overtime: Int? = null
)