package com.example.minisofascoreapp.presentation.eventlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.minisofascoreapp.R
import com.example.minisofascoreapp.domain.model.TournamentGroup
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EventListScreen(
    onEventClick: (Long) -> Unit,
    onTournamentClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: EventListViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val selectedDate by viewModel.selectedDate.collectAsState()
    val days = viewModel.daysRange

    val selectedSport by viewModel.selectedSport.collectAsState()
    val todayFormatted =
        selectedDate.format(java.time.format.DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy."))


    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Image(
                            painter = painterResource(id = R.drawable.sofascorelogo_pravi),
                            contentDescription = "Sofascore logo",
                            modifier = Modifier
                                .height(20.dp)
                                .width(132.dp)
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Navigacija za Leagues Page */ }) {
                            Image(
                                painter = painterResource(id = R.drawable.icon2),
                                contentDescription = "Leagues",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        IconButton(onClick = onSettingsClick) {
                            Image(
                                painter = painterResource(id = R.drawable.settingslogo),
                                contentDescription = "Leagues",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.sofascore_light_blue),
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.sofascore_light_blue))
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SportType.entries.forEach { sport ->
                        val isSelected = sport == selectedSport
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    viewModel.onSportSelected(sport)
                                }
                        ) {
                            Icon(
                                modifier = Modifier.size(22.dp),
                                painter = painterResource(id = sport.iconRes),
                                contentDescription = sport.label,
                                tint = if (isSelected)
                                    Color.White
                                else Color.White.copy(alpha = 0.5f)
                            )

                            Text(
                                modifier = Modifier.padding(top = 4.dp),
                                text = sport.label,
                                fontSize = 14.sp,
                                color = if (isSelected)
                                    Color.White
                                else Color.White.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.labelMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .height(4.dp)
                                        .clip(CircleShape)
                                        .fillMaxWidth()
                                        .background(Color.White)
                                )
                            }
                        }
                    }
                }

            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val listState = rememberLazyListState()

            LaunchedEffect(selectedDate) {
                val index = days.indexOfFirst { it.date == selectedDate }
                if (index != -1) {
                    listState.animateScrollToItem(index)
                }
            }

            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.sofascore_dark_blue)),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(days) { day ->
                    val isSelected = day.date == selectedDate
                    Column(
                        modifier = Modifier
                            .width(56.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { viewModel.onDateSelected(day.date) },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (day.date == LocalDate.now()) "TODAY"
                            else day.date.dayOfWeek.name.take(3),
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.labelMedium
                        )

                        Text(
                            text = "${day.date.dayOfMonth}.${day.date.monthValue}.",
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.labelSmall
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .height(5.dp)
                                    .clip(CircleShape)
                                    .fillMaxWidth()
                                    .background(Color.White)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {

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
                    if (state.value.events?.isEmpty() == true) {
                        Text(
                            text = "There are no matches on the selected day.",
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 16.sp
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            stickyHeader {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        modifier = Modifier.padding(top = 16.dp),
                                        text = todayFormatted,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        modifier = Modifier.padding(top = 16.dp),
                                        text = "${state.value.events?.sumOf { it.events.size }} Events",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            item {
                                Column(
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                ) {
                                    state.value.events?.forEach { group ->
                                        TournamentHeader(group, onTournamentClick)
                                        group.events.forEach { event ->
                                            EventItem(
                                                event = event,
                                                onEventClick = onEventClick
                                            )
                                        }
                                        HorizontalDivider(Modifier.padding(top = 8.dp))
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
fun TournamentHeader(group: TournamentGroup, onClick: (Long) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp).clickable { onClick(group.tournamentId) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = "https://academy-backend.sofascore.dev/tournament/${group.tournamentId}/image",
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = group.countryName,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                painter = painterResource(id = R.drawable.vector),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = group.tournamentName,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
