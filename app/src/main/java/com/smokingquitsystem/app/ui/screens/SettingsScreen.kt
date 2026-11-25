package com.smokingquitsystem.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smokingquitsystem.app.data.QuitProfileEntity
import com.smokingquitsystem.app.ui.theme.BorderSoft
import com.smokingquitsystem.app.ui.theme.Green
import com.smokingquitsystem.app.ui.theme.Panel
import com.smokingquitsystem.app.ui.theme.Soft
import com.smokingquitsystem.app.ui.theme.White

@Composable
fun SettingsScreen(
    profile: QuitProfileEntity,
    onLanguageChange: (String) -> Unit,
    onRemindersToggle: (Boolean) -> Unit,
    onActiveModeChange: (String) -> Unit,
    onResetSetup: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Preferences",
                color = White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            Text("Language / Zubaan", color = Soft)
            listOf("Both", "English", "Roman Urdu").forEach { option ->
                ChoiceRow(option, profile.language == option) {
                    onLanguageChange(option)
                }
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Daily reminders",
                            color = White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "5 reminders: 8 AM, 11 AM, 2 PM, 5 PM, 8 PM",
                            color = Soft,
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = profile.remindersEnabled,
                        onCheckedChange = onRemindersToggle,
                        colors = SwitchDefaults.colors(checkedThumbColor = Green)
                    )
                }
            }
        }
        item {
            Text(
                "Your quit plan / Aapka quit plan",
                color = Green,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            Text(
                "Choose your current approach",
                color = White,
                fontWeight = FontWeight.SemiBold
            )
        }
        item {
            listOf("Quit immediately", "Gradual reduction").forEach { mode ->
                ChoiceRow(mode, profile.activeMode == mode) {
                    onActiveModeChange(mode)
                }
            }
        }
        item {
            Text(
                "Available options: ${profile.method}",
                color = Soft,
                fontSize = 12.sp
            )
        }
        item {
            Text(
                "Daily cigarettes before quitting: ${profile.cigsPerDay}",
                color = White
            )
        }
        item {
            Text(
                "Pack price: PKR ${profile.packPrice}",
                color = White
            )
        }
        item {
            Text(
                "Data is stored locally on this phone in Room database. Estimated savings and avoided cigarettes are based on the details you entered.",
                color = Soft,
                fontSize = 12.sp
            )
        }
        item {
            OutlinedButton(
                onClick = onResetSetup,
                border = BorderStroke(1.dp, BorderSoft),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Green),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset app setup")
            }
        }
    }
}
