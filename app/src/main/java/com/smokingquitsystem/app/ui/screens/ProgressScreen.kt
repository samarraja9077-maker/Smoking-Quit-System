package com.smokingquitsystem.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smokingquitsystem.app.data.QuitProfileEntity
import com.smokingquitsystem.app.domain.QuitCalculations
import com.smokingquitsystem.app.domain.QuitCalculations.tr
import com.smokingquitsystem.app.ui.theme.Green
import com.smokingquitsystem.app.ui.theme.Panel
import com.smokingquitsystem.app.ui.theme.Soft
import com.smokingquitsystem.app.ui.theme.White

private data class HealthMilestone(
    val title: String,
    val description: String,
    val thresholdMs: Long
)

private val MILESTONES = listOf(
    HealthMilestone(
        "20 minutes",
        "Heart rate and blood pressure may begin to improve.",
        20L * 60_000L
    ),
    HealthMilestone(
        "12 hours",
        "Carbon monoxide level in blood can return toward normal.",
        12L * 3_600_000L
    ),
    HealthMilestone(
        "2–12 weeks",
        "Circulation and lung function may improve.",
        14L * 86_400_000L
    ),
    HealthMilestone(
        "1–9 months",
        "Coughing and shortness of breath may decrease.",
        30L * 86_400_000L
    )
)

@Composable
fun ProgressScreen(
    profile: QuitProfileEntity,
    lang: String,
    nowMillis: Long,
    sosCompletedCount: Int
) {
    val elapsed = QuitCalculations.elapsedMs(profile.quitStartMillis, nowMillis)
    val cigs = QuitCalculations.cigsAvoided(
        profile.quitStartMillis,
        nowMillis,
        profile.cigsPerDay,
        profile.activeMode
    )
    val saved = QuitCalculations.moneySaved(cigs, profile.packPrice, profile.packSize)
    val durationText = QuitCalculations.formatDuration(profile.quitStartMillis, nowMillis)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        item {
            Text(
                tr(lang, "Your progress", "Aapki taraqqi"),
                color = White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            StatCard(
                value = durationText,
                label = tr(lang, "Time since your quit start", "Quit shuru kiye hue"),
                icon = Icons.Default.Timer,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            StatCard(
                value = "$cigs",
                label = tr(lang, "Estimated cigarettes avoided", "Andazan cigarettes se bachay"),
                icon = Icons.Default.Block,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            StatCard(
                value = "PKR $saved",
                label = tr(lang, "Estimated money saved", "Andazan paisay bachay"),
                icon = Icons.Default.Savings,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (sosCompletedCount > 0) {
            item {
                StatCard(
                    value = "$sosCompletedCount",
                    label = tr(lang, "SOS cravings overcome", "SOS cravings par qaboo"),
                    icon = Icons.Default.Shield,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item {
            Text(
                tr(lang, "Recovery milestones", "Sehat ke marahil"),
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        items(MILESTONES, key = { it.title }) { milestone ->
            val reached = elapsed >= milestone.thresholdMs
            Card(
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (reached) Green else Soft
                    )
                    Column(Modifier.padding(start = 12.dp)) {
                        Text(milestone.title, color = White, fontWeight = FontWeight.Bold)
                        Text(milestone.description, color = Soft, fontSize = 12.sp)
                    }
                }
            }
        }
        item {
            Text(
                tr(
                    lang,
                    "Milestones are general educational estimates, not a personal medical prediction.",
                    "Yeh aam maloomati andazay hain, zaati medical prediction nahi."
                ),
                color = Soft,
                fontSize = 11.sp
            )
        }
    }
}
