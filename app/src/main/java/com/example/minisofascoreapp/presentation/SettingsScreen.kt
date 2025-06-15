package com.example.minisofascoreapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minisofascoreapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    selectedDateFormat: String,
    onDateFormatChange: (String) -> Unit,
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.sofascore_light_blue)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // LANGUAGE
            LanguageDropdown(selectedLanguage = selectedLanguage)

            Divider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                thickness = 1.dp
            )

            // THEME
            Column {
                Text(
                    "Theme",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.sofascore_light_blue),
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = !isDarkTheme,
                        onClick = { onThemeToggle(false) }
                    )
                    Text("Light", modifier = Modifier.padding(start = 4.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = isDarkTheme,
                        onClick = { onThemeToggle(true) }
                    )
                    Text("Dark", modifier = Modifier.padding(start = 4.dp))
                }
            }

            Divider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                thickness = 1.dp
            )

            // DATE FORMAT
            Column {
                Text(
                    "Date Format",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(id = R.color.sofascore_dark_blue),
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedDateFormat == "DD/MM/YYYY",
                        onClick = { onDateFormatChange("DD/MM/YYYY") }
                    )
                    Text("DD / MM / YYYY", modifier = Modifier.padding(start = 4.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedDateFormat == "MM/DD/YYYY",
                        onClick = { onDateFormatChange("MM/DD/YYYY") }
                    )
                    Text("MM / DD / YYYY", modifier = Modifier.padding(start = 4.dp))
                }
            }

            Divider()

// ABOUT
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("About", style = MaterialTheme.typography.titleSmall)
                Text("Sofascore {PlatformName} Academy", fontWeight = FontWeight.Bold)
                Text("Class 2025")

                Spacer(Modifier.height(4.dp))
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(Modifier.height(4.dp))
                InfoLine("App Name", "Mini Sofascore App")

                InfoLine("API Credit", "Sofascore")

                InfoLine("Developer", "Patrik Klarić")
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.sofascore_lockup),
                    contentDescription = "Sofascore Logo",
                    modifier = Modifier
                        .width(200.dp)
                        .height(60.dp)
                )
            }
        }
    }
}

@Composable
fun InfoLine(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 12.sp)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontSize = 14.sp)
    }
}

@Composable
fun LanguageDropdown(selectedLanguage: String) {
    OutlinedTextField(
        value = selectedLanguage,
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        label = { Text(
            "Language",
            style = MaterialTheme.typography.labelSmall,
            color = colorResource(id = R.color.sofascore_light_blue),
            fontSize = 14.sp
        ) },
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = LocalContentColor.current,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            disabledTrailingIconColor = LocalContentColor.current,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        enabled = false
    )
}

