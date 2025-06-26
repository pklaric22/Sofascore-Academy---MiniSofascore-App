package com.example.minisofascoreapp.presentation.tournamentDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.minisofascoreapp.domain.model.Club

@Composable
fun StandingsTabContent(
    viewModel: TournamentStandingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (state.error != null) {
        state.error?.let { error ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Error $error", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { viewModel.refresh() }) {
                    Text("Refresh")
                }
            }
        }
    } else {
        state.standings?.let { standings ->
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    HeaderRow(tournamentName = standings.tournament.name)
                }

                itemsIndexed(standings.sortedStandingsRow) { index, club ->
                    ClubRow(
                        position = index + 1,
                        club = club,
                        tournamentName = standings.tournament.name
                    )

                }
            }
        }
    }
}

@Composable
fun HeaderRow(tournamentName: String) {
    val isNBA = tournamentName == "NBA"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier
                .weight(0.9f)
                .padding(start = 4.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("#", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.width(14.dp))
            Text("Team", style = MaterialTheme.typography.labelMedium)
        }

        val columnWidth = 35.dp
        val labels = listOf("P", "W", "D", "L") +
                if (isNBA) listOf("Diff", "PCT") else listOf("Goals", "PTS")

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            labels.forEach { label ->
                Box(
                    modifier = Modifier
                        .width(columnWidth)
                        .padding(start = 1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}


@Composable
fun ClubRow(position: Int, club: Club, tournamentName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier.weight(0.9f)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = position.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                club.team.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(120.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))


        val isNBA = tournamentName == "NBA"
        val columnWidth = 33.dp
        val diff = club.scoresFor - (club.scoresAgainst)
        val pct = club.percentage

        val list = listOf(
            club.played.toString(),
            club.wins.toString(),
            club.draws.toString(),
            club.losses.toString()
        ) + if (isNBA) listOf(diff.toString(), pct.toString())
        else listOf(club.scoresFor.toString(), club.points?.toString() ?: "-")


        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            list.forEach { value ->
                Box(
                    modifier = Modifier
                        .width(columnWidth),
                    contentAlignment = Alignment.Center
                ) {
                    Text(value, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
