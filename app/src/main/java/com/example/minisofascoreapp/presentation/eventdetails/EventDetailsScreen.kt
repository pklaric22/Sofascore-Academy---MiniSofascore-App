package com.example.minisofascoreapp.presentation.eventdetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.minisofascoreapp.domain.model.EventDetails
import com.example.minisofascoreapp.domain.model.TYPE
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EventDetailsScreen(
    viewModel: EventDetailsViewModel = hiltViewModel(),
    onTournamentClick: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                state.value.details?.tournament?.let { tournament ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTournamentClick(tournament.id) }
                    ) {
                        AsyncImage(
                            model = "https://academy-backend.sofascore.dev/tournament/${tournament.id}/image",
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "${tournament.sportName}, ${tournament.countryName}, ${tournament.name}, Round ${tournament.round}",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }) { paddingValues ->
        if (state.value.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.value.error != null) {
            state.value.error?.let { error ->
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text("Refresh")
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                state.value.details?.let { details ->
                    stickyHeader {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            ScoreHeaderSection(details)
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline
                            )

                        }
                    }
                    details.incident?.let { incidents ->
                        items(incidents) { incident ->
                            when (incident.type) {
                                TYPE.CARD -> CardIncident(incident)
                                TYPE.GOAL -> GoalIncident(incident)
                                TYPE.PERIOD -> IncidentPeriodHeader(incident)
                            }
                        }
                    } ?: run {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 64.dp),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("No results yet.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = { onTournamentClick(details.tournament.id) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .height(48.dp),
                                        shape = RectangleShape,
                                        border = BorderStroke(
                                            1.dp,
                                            Color.Blue
                                        )
                                    ) {
                                        Text(
                                            text = "View Tournament Details",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ScoreHeaderSection(event: EventDetails) {
    val statusText = when (event.status.lowercase()) {
        "notstarted" -> DateTimeFormatter.ofPattern("dd.MM.yyyy.\nHH:mm").format(
            OffsetDateTime.parse(event.startDate)
        )

        "inprogress" -> {
            val minutes = Duration.between(
                OffsetDateTime.parse(event.startDate),
                OffsetDateTime.now()
            ).toMinutes().toInt()
            "$minutes'"
        }

        "finished" -> "Full Time"
        else -> event.status
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Lijeva strana: domaćin
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)) {
            AsyncImage(
                model = "https://academy-backend.sofascore.dev/team/${event.homeTeam.id}/image",
                contentDescription = null,
                modifier = Modifier.size(40.dp),

            )
            Text(
                text = event.homeTeam.name,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Sredina: rezultat i status
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)) {
            Text(
                text = "${event.homeScore.total ?: 0} - ${event.awayScore.total ?: 0}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = statusText,
                color = if (event.status.lowercase() == "inprogress") Color.Red else Color.Gray,
                style = MaterialTheme.typography.labelSmall
            )
        }

        // Desna strana: gost
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)) {
            AsyncImage(
                model = "https://academy-backend.sofascore.dev/team/${event.awayTeam.id}/image",
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Text(event.awayTeam.name)
        }
    }
}

@Composable
fun IncidentSection(incidents: List<Any>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Incident list goes here...")
    }
}
