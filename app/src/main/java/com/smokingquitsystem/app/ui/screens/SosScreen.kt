package com.smokingquitsystem.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smokingquitsystem.app.domain.QuitCalculations.tr
import com.smokingquitsystem.app.ui.theme.Bg
import com.smokingquitsystem.app.ui.theme.BorderSoft
import com.smokingquitsystem.app.ui.theme.Green
import com.smokingquitsystem.app.ui.theme.Panel
import com.smokingquitsystem.app.ui.theme.Soft
import com.smokingquitsystem.app.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun SosScreen(lang: String, onSosCompleted: () -> Unit) {
    var seconds by remember { mutableIntStateOf(120) }
    var running by remember { mutableStateOf(false) }

    LaunchedEffect(running) {
        while (running && seconds > 0) {
            delay(1000L)
            seconds--
            if (seconds == 0) {
                running = false
                onSosCompleted()
            }
        }
    }

    val tips = remember(lang) {
        listOf(
            tr(lang, "Take 5 slow breaths.", "5 aahista saans lein."),
            tr(lang, "Drink a glass of water.", "Aik glass pani piyen."),
            tr(
                lang,
                "Move away from your smoking trigger.",
                "Smoking wali jagah ya trigger se door ho jayein."
            ),
            tr(
                lang,
                "Message someone who supports you.",
                "Kisi supportive dost ya ghar walay ko message karein."
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
        contentPadding = PaddingValues(vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "header") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🫁", fontSize = 48.sp)
                Text(
                    tr(lang, "Craving SOS", "Craving ka muqabla"),
                    color = White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    tr(
                        lang,
                        "The urge will pass. Give yourself two minutes.",
                        "Yeh talab guzar jayegi. Khud ko do minute dein."
                    ),
                    color = Soft,
                    textAlign = TextAlign.Center
                )
            }
        }
        item(key = "timer") {
            Box(
                modifier = Modifier
                    .size(210.dp)
                    .background(Panel, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${seconds / 60}:${(seconds % 60).toString().padStart(2, '0')}",
                        color = Green,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (running) "BREATHE" else "PAUSE",
                        color = Soft,
                        fontSize = 12.sp
                    )
                }
            }
        }
        item(key = "actions") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        if (seconds == 0) seconds = 120
                        running = !running
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Green, contentColor = Bg),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (running) "Pause timer"
                        else if (seconds == 0) "Start again"
                        else "Start 2-minute reset",
                        fontWeight = FontWeight.SemiBold
                    )
                }
                OutlinedButton(
                    onClick = {
                        seconds = 120
                        running = false
                    },
                    border = BorderStroke(1.dp, BorderSoft),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Green),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset")
                }
            }
        }
        item(key = "tips") {
            Card(
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        tr(lang, "Try this now", "Abhi yeh karein"),
                        color = Green,
                        fontWeight = FontWeight.Bold
                    )
                    tips.forEach { tip ->
                        Text(
                            "• $tip",
                            color = White,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
        item {
            Text(
                tr(
                    lang,
                    "If withdrawal feels overwhelming, a healthcare professional can help you choose evidence-based support.",
                    "Agar withdrawal mushkil ho, healthcare professional se evidence-based madad lein."
                ),
                color = Soft,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
