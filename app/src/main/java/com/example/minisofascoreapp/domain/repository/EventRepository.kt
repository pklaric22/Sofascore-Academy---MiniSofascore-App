package com.example.minisofascoreapp.domain.repository

import com.example.minisofascoreapp.domain.model.Event
import com.example.minisofascoreapp.domain.model.EventDetails
import com.example.minisofascoreapp.utils.Result
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun getEventsForDate(
        sport: String,
        date: String
    ): Result<List<Event>>

    suspend fun getEventDetails(eventId: Long): Result<EventDetails>
}

