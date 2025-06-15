package com.example.minisofascoreapp.data.repository

import com.example.minisofascoreapp.data.remote.api.SofascoreApi
import com.example.minisofascoreapp.data.remote.dto.EventDto
import com.example.minisofascoreapp.data.remote.dto.toDomain
import com.example.minisofascoreapp.domain.model.Event
import com.example.minisofascoreapp.domain.model.Standings
import com.example.minisofascoreapp.domain.model.Tournament
import com.example.minisofascoreapp.domain.repository.TournamentRepository
import com.example.minisofascoreapp.utils.PaginatedEndpoint
import com.example.minisofascoreapp.utils.PaginatedSource
import com.example.minisofascoreapp.utils.Result
import com.example.minisofascoreapp.utils.safeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TournamentRepositoryImpl @Inject constructor(
    private val api: SofascoreApi
) : TournamentRepository {

    override suspend fun getTournamentEvents(id: Long): Flow<PaginatedSource<Event>> = flow {
        val endpoint = object : PaginatedEndpoint<EventDto, Event>(
            pageSource = { page ->
                val result = safeResponse { api.getTournamentEvents(id, page) }
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> throw result.e
                }
            },
            mapper = { list -> list.map { it.toDomain() } }
        ) {
            override val items: Flow<List<Event>> = super.items
        }

        emit(endpoint)
    }

    override suspend fun getTournamentDetails(id: Long): Result<Tournament> {
        val response = safeResponse { api.getTournamentDetails(id) }
        return when (response) {
            is Result.Error -> Result.Error(response.e)
            is Result.Success -> Result.Success(response.data.toDomain())
        }
    }

    override suspend fun getStandings(id: Long): Result<List<Standings>> {
        val response = safeResponse { api.getStandings(id) }
        return when (response) {
            is Result.Error -> Result.Error(response.e)
            is Result.Success -> Result.Success(response.data.map { it.toDomain() })
        }
    }
}