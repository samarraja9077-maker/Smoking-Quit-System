package com.smokingquitsystem.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smokingquitsystem.app.domain.QuitCalculations.tr
import com.smokingquitsystem.app.ui.theme.BorderSoft
import com.smokingquitsystem.app.ui.theme.Green
import com.smokingquitsystem.app.ui.theme.Panel
import com.smokingquitsystem.app.ui.theme.SelectedRowBg
import com.smokingquitsystem.app.ui.theme.Soft
import com.smokingquitsystem.app.ui.theme.White

@Composable
fun Field(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Number,
    onChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, color = White, fontWeight = FontWeight.Medium, fontSize = 13.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(13.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green,
                unfocusedBorderColor = BorderSoft,
                focusedTextColor = White,
                unfocusedTextColor = White,
                cursorColor = Green
            )
        )
    }
}

@Composable
fun ChoiceRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .background(if (selected) SelectedRowBg else Panel, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = Green, unselectedColor = Soft)
        )
        Text(label, color = White, modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(15.dp)) {
            Icon(icon, contentDescription = null, tint = Green)
            Text(
                value,
                color = White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(label, color = Soft, fontSize = 11.sp)
        }
    }
}

@Composable
fun MilestoneCard(lang: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                tr(lang, "A helpful health fact", "Sehat ki aik maloomat"),
                color = Green,
                fontWeight = FontWeight.Bold
            )
            Text(
                tr(
                    lang,
                    "Within about 20 minutes after your last cigarette, heart rate and blood pressure can begin moving toward normal. Recovery timelines vary from person to person.",
                    "Aakhri cigarette ke taqreeban 20 minute baad dil ki dharkan aur blood pressure normal ki taraf aana shuru ho sakte hain. Har shakhs ki recovery mukhtalif hoti hai."
                ),
                color = White,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 7.dp)
            )
        }
    }
}
