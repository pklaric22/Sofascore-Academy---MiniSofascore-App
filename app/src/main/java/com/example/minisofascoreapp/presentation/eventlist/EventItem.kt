package com.example.minisofascoreapp.presentation.eventlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.minisofascoreapp.domain.model.Event
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EventItem(
    event: Event,
    modifier: Modifier = Modifier,
    onEventClick: (Long) -> Unit,
    showDate: Boolean = false
) {
    val formattedTimeOrDate = remember(event.startDate, showDate) {
        try {
            val dateTime = OffsetDateTime.parse(event.startDate)
            if (showDate) {
                val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")
                dateTime.toLocalDate().format(dateFormatter)
            } else {
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                dateTime.toLocalTime().format(timeFormatter)
            }
        } catch (e: Exception) {
            "-"
        }
    }

    val statusLabel = when (event.status.lowercase()) {
        "finished" -> "FT"
        "inprogress" -> {
            try {
                val start = OffsetDateTime.parse(event.startDate)
                val minutes = Duration.between(start, OffsetDateTime.now()).toMinutes().toInt()
                "${minutes}'"
            } catch (e: Exception) {
                "LIVE"
            }
        }

        else -> ""
    }

    val isLive = event.status.lowercase() == "inprogress"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onEventClick(event.id)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                4.dp,
                alignment = Alignment.CenterVertically
            ),
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(text = formattedTimeOrDate, style = MaterialTheme.typography.bodySmall)
            if (statusLabel.isNotEmpty()) {
                Text(
                    text = statusLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isLive) Color.Red else Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))

        VerticalDivider(
            color = Color.LightGray,
            thickness = 1.5.dp
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = "https://academy-backend.sofascore.dev/team/${event.homeTeamId}/image",
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(event.homeTeamName, style = MaterialTheme.typography.bodyMedium)
                }

                Text(
                    text = event.homeScore?.toString() ?: "-",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isLive && event.homeScore != null && event.awayScore != null && event.homeScore < event.awayScore)
                        Color.Red else MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = "https://academy-backend.sofascore.dev/team/${event.awayTeamId}/image",
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(event.awayTeamName, style = MaterialTheme.typography.bodyMedium)
                }

                Text(
                    text = event.awayScore?.toString() ?: "-",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isLive && event.homeScore != null && event.awayScore != null && event.awayScore < event.homeScore)
                        Color.Red else MaterialTheme.colorScheme.onSurface
                )
            }
        }

    }
}




