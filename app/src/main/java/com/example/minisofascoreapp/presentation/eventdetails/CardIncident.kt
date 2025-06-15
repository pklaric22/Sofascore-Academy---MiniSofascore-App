package com.example.minisofascoreapp.presentation.eventdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minisofascoreapp.domain.model.CardColorType
import com.example.minisofascoreapp.domain.model.Incident
import com.example.minisofascoreapp.domain.model.TeamSide

@Composable
fun CardIncident(incident: Incident) {
    val isHome = incident.teamSide == TeamSide.HOME
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalArrangement = if (isHome) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isHome) {
            CardContentLeft(incident)
        } else {
            CardContentRight(incident)
        }
    }
}

@Composable
fun CardContentLeft(incident: Incident) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(40.dp)) {
        CardBoxWithMinute(incident)
        Spacer(modifier = Modifier.width(16.dp))
        VerticalDivider(color = Color.LightGray, thickness = 1.5.dp)
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = incident.playerName ?: "", fontWeight = FontWeight.Medium, fontSize = 12.sp)
    }
}

@Composable
fun CardContentRight(incident: Incident) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(40.dp)) {
        Text(text = incident.playerName ?: "", fontWeight = FontWeight.Medium, fontSize = 12.sp)
        Spacer(modifier = Modifier.width(16.dp))
        VerticalDivider(color = Color.LightGray, thickness = 1.5.dp)
        Spacer(modifier = Modifier.width(24.dp))
        CardBoxWithMinute(incident)
    }
}

@Composable
fun CardBoxWithMinute(incident: Incident) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when (incident.cardColorType) {
            CardColorType.YELLOW -> {
                Box(
                    modifier = Modifier
                        .size(width = 14.dp, height = 20.dp)
                        .background(Color.Yellow)
                )
            }

            CardColorType.RED -> {
                Box(
                    modifier = Modifier
                        .size(width = 14.dp, height = 20.dp)
                        .background(Color.Red)
                        .padding(2.dp)
                )
            }

            CardColorType.YELLOW_RED -> {
                Row {
                    Box(
                        modifier = Modifier
                            .size(width = 10.dp, height = 20.dp)
                            .background(Color.Yellow)
                            .padding(2.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(width = 10.dp, height = 20.dp)
                            .background(Color.Red)
                            .padding(2.dp)
                    )
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .size(width = 20.dp, height = 20.dp)
                        .background(Color.Gray)
                        .padding(2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text("${incident.minute}'", fontSize = 10.sp, color = Color.Gray)
    }
}
