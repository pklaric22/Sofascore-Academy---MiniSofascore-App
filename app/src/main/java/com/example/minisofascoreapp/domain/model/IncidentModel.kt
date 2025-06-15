package com.example.minisofascoreapp.domain.model

data class Incident(
    val id: Long,
    val text: String?,
    val type: TYPE,
    val minute: Int,
    val playerName: String?,
    val teamSide: TeamSide?,
    val scoringTeam: TeamSide?,
    val goalType: GoalType?,
    val cardColorType: CardColorType?,
    val homeScore: Int?,
    val awayScore: Int?
)

enum class TeamSide {
    HOME, AWAY
}

enum class TYPE {
    CARD, GOAL, PERIOD,
}

enum class CardColorType {
    YELLOW,
    YELLOW_RED,
    RED
}

enum class GoalType {
    REGULAR,
    OWN_GOAL,
    PENALTY,
    ONE_POINT,
    TWO_POINT,
    THREE_POINT,
    TOUCHDOWN,
    SAFETY,
    FIELD_GOAL,
    EXTRA_POINT
}