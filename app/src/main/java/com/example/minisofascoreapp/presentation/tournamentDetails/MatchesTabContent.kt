package com.example.minisofascoreapp.presentation.tournamentDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.minisofascoreapp.domain.model.Event
import com.example.minisofascoreapp.domain.model.dateGroupLabel
import com.example.minisofascoreapp.domain.model.isPast
import com.example.minisofascoreapp.presentation.eventlist.EventItem
import kotlinx.coroutines.flow.map

@Composable
fun MatchesTabContent(
    onEventClick: (Long) -> Unit,
    viewModel: TournamentEventsViewModel = hiltViewModel()
) {
    val uiItems = viewModel.eventsWithSeparators.collectAsLazyPagingItems()
    val listState = rememberLazyListState()

    LaunchedEffect(uiItems.loadState.refresh) {
        if (uiItems.loadState.refresh is LoadState.NotLoading) {
            val index = uiItems.itemSnapshotList.items.indexOfFirst {
                it is EventUiItem.EventItem && !it.event.isPast()
            }
            if (index >= 0) {
                listState.scrollToItem(index)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = uiItems.itemCount,
            key = uiItems.itemKey {
                when (it) {
                    is EventUiItem.EventItem -> "event_${it.event.id}"
                    is EventUiItem.Separator -> "separator_${it.label}"
                    null -> "unknown"
                }
            }
        ) { index ->
            when (val item = uiItems[index]) {
                is EventUiItem.Separator -> {
                    Text(
                        text = item.label,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                is EventUiItem.EventItem -> {
                    EventItem(
                        event = item.event,
                        onEventClick = onEventClick,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        showDate = true
                    )
                }

                else -> Unit
            }
        }

        item {
            when (val append = uiItems.loadState.append) {
                is LoadState.Loading -> {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }

                is LoadState.Error -> {
                    Button(
                        onClick = { uiItems.retry() },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Retry")
                    }
                }

                else -> Unit
            }
        }
    }

    when (val refresh = uiItems.loadState.refresh) {
        is LoadState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error loading events", color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { uiItems.retry() }) {
                        Text("Try Again")
                    }
                }
            }
        }

        else -> Unit
    }
}
