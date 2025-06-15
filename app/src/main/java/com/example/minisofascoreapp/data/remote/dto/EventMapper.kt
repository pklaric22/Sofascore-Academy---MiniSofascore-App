package com.example.minisofascoreapp.data.remote.dto

import com.example.minisofascoreapp.domain.model.Event
import com.example.minisofascoreapp.domain.model.Tournament

fun EventDto.toDomain(): Event {
    return Event(
        id = id,
        tournamentName = tournament.name,
        homeTeamName = homeTeam.name,
        awayTeamName = awayTeam.name,
        status = status,
        startDate = startDate,
        homeScore = homeScore.total,
        awayScore = awayScore.total,
        countryName = tournament.country.name,
        sportName = tournament.sport.name,
        homeTeamId = homeTeam.id,
        awayTeamId = awayTeam.id,
        tournamentId = tournament.id,
        tournamentCountryName = tournament.country.name,
        round = round ?: 0
    )
}

fun TournamentDto.toDomain(): Tournament {
    return Tournament(
        id = id,
        name = name,
        countryName = country.name,
        sportName = sport.name,
        round = null
    )
}
