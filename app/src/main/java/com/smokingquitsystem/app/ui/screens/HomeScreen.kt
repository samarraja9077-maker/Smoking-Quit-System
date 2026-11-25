package com.smokingquitsystem.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.smokingquitsystem.app.ui.theme.ReminderCardBg
import com.smokingquitsystem.app.ui.theme.Soft
import com.smokingquitsystem.app.ui.theme.White

@Composable
fun HomeScreen(profile: QuitProfileEntity, lang: String, nowMillis: Long) {
    val day = QuitCalculations.daysOnJourney(profile.quitStartMillis, nowMillis)
    val cigs = QuitCalculations.cigsAvoided(
        profile.quitStartMillis,
        nowMillis,
        profile.cigsPerDay,
        profile.activeMode
    )
    val money = QuitCalculations.moneySaved(cigs, profile.packPrice, profile.packSize)
    val durationText = QuitCalculations.formatDuration(profile.quitStartMillis, nowMillis)
    val title = tr(lang, "Your smoke-free journey", "Aapka smoke-free safar")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(title, color = Soft, fontSize = 13.sp)
                    Text(
                        "$day",
                        color = Green,
                        fontSize = 64.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        tr(lang, "DAYS ON QUIT JOURNEY", "QUIT JOURNEY KE DIN"),
                        color = White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        durationText,
                        color = Soft,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        tr(
                            lang,
                            "One moment at a time. Keep going.",
                            "Bas aik lamha aik waqt mein. Aagay barhtay rahain."
                        ),
                        color = Green,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard(
                    value = "$cigs",
                    label = tr(lang, "Cigarettes avoided", "Cigarettes se bachay"),
                    icon = Icons.Default.Block,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = "Rs $money",
                    label = tr(lang, "Estimated saved", "Andazan bachay"),
                    icon = Icons.Default.Savings,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ReminderCardBg),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(17.dp)) {
                    Text(
                        tr(lang, "TODAY'S REMINDER", "AAJ KI YAAD DEHANI"),
                        color = Green,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        tr(
                            lang,
                            "A craving is a wave. You don't have to act on it. Pause, breathe, and let it pass.",
                            "Craving aik lehar ki tarah hai. Is par amal zaroori nahi. Rukain, saans lein aur guzarne dein."
                        ),
                        color = White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
        item {
            Text(
                tr(lang, "Your next small win", "Aapki agli choti jeet"),
                color = White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            val mode = profile.activeMode
            val base = profile.cigsPerDay
            val target = QuitCalculations.gradualTargetPerDay(day, base, mode)
            Card(
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        tr(lang, "YOUR CURRENT APPROACH", "AAPKA MAUJOODA TAREEQA"),
                        color = Green,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Text(
                        if (mode == "Gradual reduction") {
                            tr(lang, "Gradual reduction", "Aahista aahista kami")
                        } else {
                            tr(lang, "Quit immediately", "Foran quit")
                        },
                        color = White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    if (mode == "Gradual reduction") {
                        Text(
                            tr(
                                lang,
                                "Suggested target: about $target cigarettes/day (from $base). Adjust with a healthcare professional if needed.",
                                "Tajweez shuda target: taqreeban $target cigarettes rozana (pehle $base). Zaroorat par healthcare professional se mashwara karein."
                            ),
                            color = Soft,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    } else {
                        Text(
                            tr(
                                lang,
                                "Focus on staying smoke-free, one craving at a time.",
                                "Har craving ka aik aik kar ke muqabla karein aur smoking se door rahain."
                            ),
                            color = Soft,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
        }
        item { MilestoneCard(lang) }
    }
}
