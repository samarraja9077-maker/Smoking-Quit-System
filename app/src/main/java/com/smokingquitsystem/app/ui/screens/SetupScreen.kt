package com.smokingquitsystem.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smokingquitsystem.app.ui.theme.Bg
import com.smokingquitsystem.app.ui.theme.Green
import com.smokingquitsystem.app.ui.theme.Soft
import com.smokingquitsystem.app.ui.theme.White

@Composable
fun SetupScreen(
    initialLang: String,
    onDone: (Int, Float, Int, String, String) -> Unit
) {
    var daily by remember { mutableStateOf("10") }
    var price by remember { mutableStateOf("500") }
    var pack by remember { mutableStateOf("20") }
    var method by remember { mutableStateOf("Both") }
    var lang by remember { mutableStateOf(initialLang) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
        contentPadding = PaddingValues(vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(Modifier.height(12.dp))
            Text("🚭", fontSize = 42.sp)
            Text(
                "Smoking Quit System",
                color = Green,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text("By Jawad", color = Soft, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(
                "Your fresh start begins here",
                color = White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Aapka naya safar yahan se shuru hota hai. No judgement, just progress.",
                color = Soft,
                fontSize = 14.sp
            )
        }
        item {
            Field("Cigarettes per day / Roz kitni cigarettes?", daily, KeyboardType.Number) {
                daily = it
            }
        }
        item {
            Field("Price per pack (PKR) / Packet ki qeemat", price, KeyboardType.Decimal) {
                price = it
            }
        }
        item {
            Field("Cigarettes in one pack / Packet mein cigarettes", pack, KeyboardType.Number) {
                pack = it
            }
        }
        item {
            Text(
                "Choose your quit approach / Quit karne ka tareeqa",
                color = White,
                fontWeight = FontWeight.SemiBold
            )
            listOf("Both", "Gradual reduction", "Quit immediately").forEach { option ->
                ChoiceRow(option, method == option) { method = option }
            }
        }
        item {
            Text("App language", color = White, fontWeight = FontWeight.SemiBold)
            listOf("Both", "English", "Roman Urdu").forEach { option ->
                ChoiceRow(option, lang == option) { lang = option }
            }
        }
        item {
            Button(
                onClick = {
                    onDone(
                        daily.toIntOrNull()?.coerceIn(1, 100) ?: 10,
                        price.toFloatOrNull()?.coerceAtLeast(0f) ?: 500f,
                        pack.toIntOrNull()?.coerceIn(1, 100) ?: 20,
                        method,
                        lang
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green, contentColor = Bg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Start my smoke-free journey  →", fontWeight = FontWeight.Bold)
            }
            Text(
                "You can change these details anytime in Settings.",
                color = Soft,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}
