package com.example.minisofascoreapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.minisofascoreapp.data.remote.api.SofascoreApi
import com.example.minisofascoreapp.data.remote.dto.toDomain
import com.example.minisofascoreapp.domain.model.Event

class TournamentEventsPagingSource(
    private val api: SofascoreApi,
    private val tournamentId: Long
) : PagingSource<PageKey, Event>() {

    override suspend fun load(params: LoadParams<PageKey>): LoadResult<PageKey, Event> {
        val key = params.key ?: PageKey("next", 0)

        return try {
            val raw = when (key.span) {
                "next" -> api.getTournamentEventsNext(tournamentId, key.page)
                "last" -> api.getTournamentEventsLast(tournamentId, key.page)
                else -> emptyList()
            }

            val events = raw.map { it.toDomain() }

            LoadResult.Page(
                data = events,
                prevKey = if (events.isNotEmpty()) {
                    when (key.span) {
                        "next" -> PageKey("last", 0)
                        "last" -> key.copy(page = key.page + 1)
                        else -> null
                    }
                } else null,
                nextKey = if (events.isNotEmpty()) {
                    when (key.span) {
                        "last" -> PageKey("next", 0)
                        "next" -> key.copy(page = key.page + 1)
                        else -> null
                    }
                } else null
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<PageKey, Event>): PageKey? {
        return PageKey("next", 0)
    }
}
