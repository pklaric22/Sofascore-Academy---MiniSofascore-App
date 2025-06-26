package com.example.minisofascoreapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.minisofascoreapp.data.remote.api.SofascoreApi
import com.example.minisofascoreapp.data.remote.dto.toDomain
import com.example.minisofascoreapp.domain.model.Event
import com.example.minisofascoreapp.domain.model.EventDetails
import com.example.minisofascoreapp.domain.repository.EventRepository
import com.example.minisofascoreapp.utils.Result
import com.example.minisofascoreapp.utils.safeResponse
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val api: SofascoreApi
) : EventRepository {

    override suspend fun getEventsForDate(sport: String, date: String): Result<List<Event>> {
        val response = safeResponse { api.getEventsForDate(sport, date) }
        return when (response) {
            is Result.Error -> Result.Error(response.e)
            is Result.Success -> Result.Success(response.data.map { it.toDomain() })
        }
    }

    override suspend fun getEventDetails(id: Long): Result<EventDetails> {
        val eventResponse = safeResponse { api.getEventDetails(id) }
        val incidentResponse = safeResponse { api.getEventIncident(id) }

        return when {
            eventResponse is Result.Error -> Result.Error(eventResponse.e)
            incidentResponse is Result.Error -> Result.Error(incidentResponse.e)
            eventResponse is Result.Success && incidentResponse is Result.Success -> {
                val event = eventResponse.data
                val incidents = incidentResponse.data
                Result.Success(event.toDomain(incidents))
            }

            else -> Result.Error(Exception("Unknown error"))
        }
    }

}
