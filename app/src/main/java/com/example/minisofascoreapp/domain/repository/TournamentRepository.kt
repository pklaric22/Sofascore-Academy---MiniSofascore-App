package com.example.minisofascoreapp.domain.repository

import androidx.paging.PagingData
import com.example.minisofascoreapp.data.remote.dto.EventDto
import com.example.minisofascoreapp.domain.model.Event
import com.example.minisofascoreapp.domain.model.Standings
import com.example.minisofascoreapp.domain.model.Tournament
import com.example.minisofascoreapp.utils.PaginatedSource
import com.example.minisofascoreapp.utils.Result
import kotlinx.coroutines.flow.Flow

interface TournamentRepository {
    suspend fun getTournamentEventsNext(tournamentId: Long, page: Int): List<EventDto>
    suspend fun getTournamentEventsLast(tournamentId: Long, page: Int): List<EventDto>
    suspend fun getTournamentDetails(id: Long): Result<Tournament>
    suspend fun getStandings(id: Long): Result<List<Standings>>
    fun getPagedTournamentEvents(id: Long): Flow<PagingData<Event>>
}

