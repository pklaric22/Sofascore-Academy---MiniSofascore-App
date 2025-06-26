package com.example.minisofascoreapp.data.remote.dto

import com.example.minisofascoreapp.domain.model.CardColorType
import com.example.minisofascoreapp.domain.model.EventDetails
import com.example.minisofascoreapp.domain.model.GoalType
import com.example.minisofascoreapp.domain.model.Incident
import com.example.minisofascoreapp.domain.model.Score
import com.example.minisofascoreapp.domain.model.TYPE
import com.example.minisofascoreapp.domain.model.Team
import com.example.minisofascoreapp.domain.model.TeamSide
import com.example.minisofascoreapp.domain.model.Tournament

fun EventDetailsDto.toDomain(incidentsDto: List<IncidentDto>?): EventDetails {
    return EventDetails(
        id = id,
        status = status,
        startDate = startDate,
        round = round,
        homeTeam = Team(
            id = homeTeam.id,
            name = homeTeam.name,
            countryName = homeTeam.country.name
        ),
        awayTeam = Team(
            id = awayTeam.id,
            name = awayTeam.name,
            countryName = awayTeam.country.name
        ),
        tournament = Tournament(
            id = tournament.id,
            name = tournament.name,
            countryName = tournament.country.name,
            sportName = tournament.sport.name,
            round = round
        ),
        homeScore = Score(
            total = homeScore.total,
            period1 = homeScore.period1,
            period2 = homeScore.period2,
            period3 = homeScore.period3,
            period4 = homeScore.period4,
            overtime = homeScore.overtime
        ),
        awayScore = Score(
            total = awayScore.total,
            period1 = awayScore.period1,
            period2 = awayScore.period2,
            period3 = awayScore.period3,
            period4 = awayScore.period4,
            overtime = awayScore.overtime
        ),
        incident = incidentsDto?.map { it.toDomain() }?.sortedByDescending { it.minute }
    )
}

fun IncidentDto.toDomain(): Incident = Incident(
    id = id,
    type = when (type.lowercase()) {
        "goal" -> TYPE.GOAL
        "card" -> TYPE.CARD
        "period" -> TYPE.PERIOD
        else -> error("Unknown type: $type")
    },
    minute = time,
    playerName = player?.name,
    teamSide = when (teamSide?.lowercase()) {
        "home" -> TeamSide.HOME
        "away" -> TeamSide.AWAY
        else -> null
    },
    scoringTeam = when (scoringTeam?.lowercase()) {
        "home" -> TeamSide.HOME
        "away" -> TeamSide.AWAY
        else -> null
    },
    goalType = goalType?.toGoalType(),
    cardColorType = color?.toCardColorType(),
    text = text,
    homeScore = homeScore,
    awayScore = awayScore
)

fun String.toCardColorType(): CardColorType? = when (this.lowercase()) {
    "yellow" -> CardColorType.YELLOW
    "yellowred" -> CardColorType.YELLOW_RED
    "red" -> CardColorType.RED
    else -> null
}

fun String.toGoalType(): GoalType? = when (this.lowercase()) {
    "regular" -> GoalType.REGULAR
    "owngoal" -> GoalType.OWN_GOAL
    "penalty" -> GoalType.PENALTY
    "onepoint" -> GoalType.ONE_POINT
    "twopoint" -> GoalType.TWO_POINT
    "threepoint" -> GoalType.THREE_POINT
    "touchdown" -> GoalType.TOUCHDOWN
    "safety" -> GoalType.SAFETY
    "fieldgoal" -> GoalType.FIELD_GOAL
    "extrapoint" -> GoalType.EXTRA_POINT
    else -> null
}