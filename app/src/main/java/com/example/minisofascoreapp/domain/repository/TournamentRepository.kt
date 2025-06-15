package com.example.minisofascoreapp.domain.repository

import com.example.minisofascoreapp.domain.model.Event
import com.example.minisofascoreapp.domain.model.Standings
import com.example.minisofascoreapp.domain.model.Tournament
import com.example.minisofascoreapp.utils.PaginatedSource
import com.example.minisofascoreapp.utils.Result
import kotlinx.coroutines.flow.Flow

interface TournamentRepository {
    suspend fun getTournamentEvents(id: Long): Flow<PaginatedSource<Event>>
    suspend fun getTournamentDetails(id: Long): Result<Tournament>
    suspend fun getStandings(id: Long): Result<List<Standings>>
}

