package com.smokingquitsystem.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smokingquitsystem.app.domain.QuitCalculations.tr
import com.smokingquitsystem.app.ui.theme.Green
import com.smokingquitsystem.app.ui.theme.Panel
import com.smokingquitsystem.app.ui.theme.White

private val ENGLISH_QUOTES = listOf(
    "You are stronger than a temporary craving.",
    "Don't count the days. Make the days count.",
    "Every cigarette you skip is a choice for your future.",
    "A slip is not the end. Learn, reset, continue.",
    "You deserve a life that isn't controlled by nicotine.",
    "Small wins become a powerful new habit."
)

private val ROMAN_QUOTES = listOf(
    "Aap aik aarzi craving se zyada mazboot hain.",
    "Sirf din na ginein, har din ko qeemti banayein.",
    "Har chori hui cigarette aapke mustaqbil ke haq mein faisla hai.",
    "Aik slip ka matlab haar nahi. Seekhein, dobara shuru karein.",
    "Aap aisi zindagi deserve karte hain jo nicotine ke qaboo mein na ho.",
    "Choti jeetain mil kar bari kamyabi banti hain."
)

@Composable
fun QuotesScreen(lang: String) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                tr(lang, "A little motivation", "Thora sa hosla"),
                color = White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        items(ENGLISH_QUOTES.size, key = { it }) { index ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Panel),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        "“",
                        color = Green,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        tr(lang, ENGLISH_QUOTES[index], ROMAN_QUOTES[index]),
                        color = White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 8.dp, top = 7.dp)
                    )
                }
            }
        }
    }
}
