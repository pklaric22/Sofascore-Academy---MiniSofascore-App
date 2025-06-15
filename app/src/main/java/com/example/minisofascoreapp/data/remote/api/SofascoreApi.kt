package com.example.minisofascoreapp.data.remote.api

import com.example.minisofascoreapp.data.remote.dto.EventDetailsDto
import com.example.minisofascoreapp.data.remote.dto.EventDto
import com.example.minisofascoreapp.data.remote.dto.IncidentDto
import com.example.minisofascoreapp.data.remote.dto.StandingsDto
import com.example.minisofascoreapp.data.remote.dto.TournamentDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SofascoreApi {

    @GET("sport/{slug}/events/{date}")
    suspend fun getEventsForDate(
        @Path("slug") sport: String,      // npr. "football"
        @Path("date") date: String        // npr. "2025-06-11"
    ): List<EventDto>

    @GET("event/{id}")
    suspend fun getEventDetails(@Path("id") id: Long): EventDetailsDto

    @GET("event/{id}/incidents")
    suspend fun getEventIncident(@Path("id") id: Long): List<IncidentDto>

    @GET("/tournament/{id}/events/last/{page}")
    suspend fun getTournamentEvents(
        @Path("id") teamId: Long,
        @Path("page") page: Int
    ): List<EventDto>

    @GET("tournament/{id}")
    suspend fun getTournamentDetails(@Path("id") id: Long): TournamentDto

    @GET("tournament/{id}/standings")
    suspend fun getStandings(@Path("id") id: Long):List<StandingsDto>

}
