package com.example.minisofascoreapp.presentation.eventdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minisofascoreapp.R
import com.example.minisofascoreapp.domain.model.Incident
import com.example.minisofascoreapp.domain.model.TeamSide

@Composable
fun GoalIncident(incident: Incident) {
    val isHome = incident.scoringTeam == TeamSide.HOME
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = if (isHome) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isHome) {
            GoalContentLeft(incident)
        } else {
            GoalContentRight(incident)
        }
    }
}

@Composable
fun GoalContentLeft(incident: Incident) {
    val scoreText = "${incident.homeScore} - ${incident.awayScore}"
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(40.dp)) {
        GoalIconWithMinute(incident)
        Spacer(modifier = Modifier.width(16.dp))
        VerticalDivider(
            color = Color.LightGray,
            thickness = 1.5.dp
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = scoreText, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = incident.playerName ?: "", fontWeight = FontWeight.Medium, fontSize = 16.sp)
    }
}

@Composable
fun GoalContentRight(incident: Incident) {
    val scoreText = "${incident.homeScore} - ${incident.awayScore}"
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(40.dp)) {
        Text(text = incident.playerName ?: "", fontWeight = FontWeight.Medium, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = scoreText, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Spacer(modifier = Modifier.width(16.dp))
        VerticalDivider(
            color = Color.LightGray,
            thickness = 1.5.dp
        )
        Spacer(modifier = Modifier.width(16.dp))
        GoalIconWithMinute(incident)
    }
}

@Composable
fun GoalIconWithMinute(incident: Incident) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.coloredshape),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Text("${incident.minute}'", fontSize = 10.sp, color = Color.Gray)
    }
}
