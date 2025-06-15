package com.example.minisofascoreapp.presentation.tournamentDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.minisofascoreapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentDetailsScreen(
    onBackClick: () -> Unit,
    onEventClick: (Long) -> Unit,
    viewModel: TournamentDetailsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(TournamentTab.Matches) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.sofascore_light_blue),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                )
            )
        }
    ) { paddingValues ->
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
            Column(modifier = Modifier.padding(paddingValues)) {
                state.details?.let { details ->
                    TournamentHeaderSection(
                        id = details.id.toString(),
                        tournamentName = details.name,
                        countryName = details.countryName
                    )

                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = Color.White.copy(alpha = 0.3f)
                    )
                }

                TabRow(
                    modifier = Modifier.height(54.dp),
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = colorResource(id = R.color.sofascore_light_blue),
                    divider = {},
                    indicator = { tabPositions ->
                        val currentTabPosition = tabPositions[selectedTab.ordinal]
                        Box(
                            modifier = Modifier
                                .wrapContentSize(Alignment.BottomStart)
                                .offset(x = currentTabPosition.left)
                                .width(currentTabPosition.width)
                                .padding(horizontal = 4.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                ) {
                    TournamentTab.entries.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedTab.ordinal == index,
                            onClick = { selectedTab = tab },
                            text = {
                                Text(
                                    text = tab.label,
                                    color = if (selectedTab.ordinal == index)
                                        Color.White
                                    else Color.White.copy(alpha = 0.5f)
                                )
                            }
                        )
                    }
                }


                when (selectedTab) {
                    TournamentTab.Matches -> MatchesTabContent(onEventClick)

                    TournamentTab.Standings -> StandingsTabContent()
                }
            }
        }
    }
}

@Composable
fun TournamentHeaderSection(
    id: String,
    tournamentName: String,
    countryName: String,
) {

    val countryFlags = mapOf(
        "England" to "https://flagcdn.com/w40/gb-eng.png",
        "Croatia" to "https://flagcdn.com/w40/hr.png",
        "Spain" to "https://flagcdn.com/w40/es.png"
    )

    val flagUrl = countryFlags[countryName]


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.sofascore_light_blue))
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = "https://academy-backend.sofascore.dev/tournament/${id}/image",
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(6.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = tournamentName,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (flagUrl != null) {
                    AsyncImage(
                        model = flagUrl,
                        contentDescription = "$countryName flag",
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                }

                Text(
                    text = countryName,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
    }
}

enum class TournamentTab(val label: String) {
    Matches("Matches"),
    Standings("Standings")
}


