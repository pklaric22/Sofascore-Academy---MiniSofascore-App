package com.example.minisofascoreapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.minisofascoreapp.data.remote.api.SofascoreApi
import com.example.minisofascoreapp.data.remote.dto.EventDto
import com.example.minisofascoreapp.data.remote.dto.toDomain
import com.example.minisofascoreapp.data.remote.paging.PageKey
import com.example.minisofascoreapp.data.remote.paging.TournamentEventsPagingSource
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

    override suspend fun getTournamentEventsNext(tournamentId: Long, page: Int): List<EventDto> {
        return api.getTournamentEventsNext(tournamentId, page)
    }

    override suspend fun getTournamentEventsLast(tournamentId: Long, page: Int): List<EventDto> {
        return api.getTournamentEventsLast(tournamentId, page)
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

    override fun getPagedTournamentEvents(id: Long): Flow<PagingData<Event>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            initialKey = PageKey("next", 0),
            pagingSourceFactory = { TournamentEventsPagingSource(api, id) }
        ).flow
    }

}