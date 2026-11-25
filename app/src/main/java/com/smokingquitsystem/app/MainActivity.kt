package com.smokingquitsystem.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smokingquitsystem.app.domain.QuitCalculations
import com.smokingquitsystem.app.notifications.ReminderScheduler
import com.smokingquitsystem.app.ui.QuitViewModel
import com.smokingquitsystem.app.ui.screens.HomeScreen
import com.smokingquitsystem.app.ui.screens.ProgressScreen
import com.smokingquitsystem.app.ui.screens.QuotesScreen
import com.smokingquitsystem.app.ui.screens.SettingsScreen
import com.smokingquitsystem.app.ui.screens.SetupScreen
import com.smokingquitsystem.app.ui.screens.SosScreen
import com.smokingquitsystem.app.ui.theme.Bg
import com.smokingquitsystem.app.ui.theme.Green
import com.smokingquitsystem.app.ui.theme.NavIndicatorBg
import com.smokingquitsystem.app.ui.theme.Panel
import com.smokingquitsystem.app.ui.theme.SmokingQuitTheme
import com.smokingquitsystem.app.ui.theme.Soft
import com.smokingquitsystem.app.ui.theme.White
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        ReminderScheduler.createNotificationChannel(this)
        setContent {
            SmokingQuitApp()
        }
    }
}

private data class NavTab(
    val label: String,
    val icon: ImageVector,
    val key: String
)

private val NAV_TABS = listOf(
    NavTab("Home", Icons.Default.Home, "Home"),
    NavTab("Progress", Icons.Default.Insights, "Progress"),
    NavTab("SOS", Icons.Default.Favorite, "SOS"),
    NavTab("Quotes", Icons.Default.FormatQuote, "Quotes"),
    NavTab("Settings", Icons.Default.Settings, "Settings")
)

@Composable
fun SmokingQuitApp(viewModel: QuitViewModel = viewModel()) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sosCount by viewModel.sosCount.collectAsStateWithLifecycle()

    var page by remember { mutableStateOf("Home") }
    var nowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            nowMillis = System.currentTimeMillis()
        }
    }

    fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    SmokingQuitTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            color = Bg
        ) {
            if (!uiState.loaded) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Green)
                }
            } else if (!uiState.profile.configured) {
                SetupScreen(
                    initialLang = uiState.profile.language,
                    onDone = { daily, price, pack, method, lang ->
                        viewModel.completeSetup(daily, price, pack, method, lang)
                        requestNotificationPermissionIfNeeded()
                        page = "Home"
                    }
                )
            } else {
                val lang = uiState.profile.language
                Column(Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "SMOKING QUIT SYSTEM",
                                color = Green,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.4.sp
                            )
                            Text(
                                QuitCalculations.pageTitle(page, lang),
                                color = White,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "By Jawad",
                                color = Soft,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(Panel, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Eco, contentDescription = null, tint = Green)
                        }
                    }

                    Box(Modifier.weight(1f)) {
                        when (page) {
                            "Home" -> HomeScreen(uiState.profile, lang, nowMillis)
                            "Progress" -> ProgressScreen(
                                uiState.profile,
                                lang,
                                nowMillis,
                                sosCount
                            )
                            "SOS" -> SosScreen(lang) {
                                viewModel.recordSosCompleted()
                            }
                            "Quotes" -> QuotesScreen(lang)
                            else -> SettingsScreen(
                                profile = uiState.profile,
                                onLanguageChange = { viewModel.updateLanguage(it) },
                                onRemindersToggle = { enabled ->
                                    if (enabled) requestNotificationPermissionIfNeeded()
                                    viewModel.updateReminders(enabled)
                                },
                                onActiveModeChange = { viewModel.updateActiveMode(it) },
                                onResetSetup = { viewModel.resetSetup() }
                            )
                        }
                    }

                    NavigationBar(containerColor = Panel, contentColor = Green) {
                        NAV_TABS.forEach { tab ->
                            NavigationBarItem(
                                selected = page == tab.key,
                                onClick = { page = tab.key },
                                icon = { Icon(tab.icon, contentDescription = tab.label) },
                                label = {
                                    Text(
                                        QuitCalculations.tr(lang, tab.label, tab.label),
                                        fontSize = 10.sp
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Green,
                                    selectedTextColor = Green,
                                    indicatorColor = NavIndicatorBg,
                                    unselectedIconColor = Soft,
                                    unselectedTextColor = Soft
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
