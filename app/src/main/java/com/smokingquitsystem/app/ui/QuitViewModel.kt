package com.smokingquitsystem.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smokingquitsystem.app.data.CravingLogEntity
import com.smokingquitsystem.app.data.QuitDatabase
import com.smokingquitsystem.app.data.QuitProfileEntity
import com.smokingquitsystem.app.data.ReminderPrefsSync
import com.smokingquitsystem.app.notifications.ReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class QuitUiState(
    val loaded: Boolean = false,
    val profile: QuitProfileEntity = QuitProfileEntity(),
    val sosCompletedCount: Int = 0
)

class QuitViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = QuitDatabase.getInstance(application).quitDao()

    val uiState: StateFlow<QuitUiState> = dao.observeProfile()
        .map { entity ->
            QuitUiState(
                loaded = true,
                profile = entity ?: QuitProfileEntity()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = QuitUiState(loaded = false)
        )

    val sosCount: StateFlow<Int> = dao.observeCravingLogCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    fun completeSetup(
        daily: Int,
        price: Float,
        packSize: Int,
        method: String,
        language: String
    ) {
        val activeMode = if (method == "Gradual reduction") "Gradual reduction" else "Quit immediately"
        val profile = QuitProfileEntity(
            id = 1,
            configured = true,
            quitStartMillis = System.currentTimeMillis(),
            cigsPerDay = daily,
            packPrice = price,
            packSize = packSize,
            method = method,
            activeMode = activeMode,
            language = language,
            remindersEnabled = true
        )
        viewModelScope.launch {
            dao.upsertProfile(profile)
            val ctx = getApplication<Application>()
            ReminderPrefsSync.sync(ctx, true, language)
            ReminderScheduler.scheduleReminders(ctx)
        }
    }

    fun updateLanguage(language: String) {
        viewModelScope.launch {
            val current = dao.getProfileOnce() ?: QuitProfileEntity()
            val updated = current.copy(language = language)
            dao.upsertProfile(updated)
            val ctx = getApplication<Application>()
            ReminderPrefsSync.sync(ctx, updated.remindersEnabled, language)
            if (updated.remindersEnabled) {
                ReminderScheduler.scheduleReminders(ctx)
            }
        }
    }

    fun updateReminders(enabled: Boolean) {
        viewModelScope.launch {
            val current = dao.getProfileOnce() ?: QuitProfileEntity()
            val updated = current.copy(remindersEnabled = enabled)
            dao.upsertProfile(updated)
            val ctx = getApplication<Application>()
            ReminderPrefsSync.sync(ctx, enabled, updated.language)
            if (enabled) {
                ReminderScheduler.scheduleReminders(ctx)
            } else {
                ReminderScheduler.cancelReminders(ctx)
            }
        }
    }

    fun updateActiveMode(mode: String) {
        viewModelScope.launch {
            val current = dao.getProfileOnce() ?: QuitProfileEntity()
            dao.upsertProfile(current.copy(activeMode = mode))
        }
    }

    fun resetSetup() {
        viewModelScope.launch {
            val current = dao.getProfileOnce() ?: QuitProfileEntity()
            dao.upsertProfile(current.copy(configured = false))
        }
    }

    fun recordSosCompleted() {
        viewModelScope.launch {
            dao.insertCravingLog(
                CravingLogEntity(
                    timestampMillis = System.currentTimeMillis(),
                    type = "sos_completed",
                    note = "Completed 2-minute SOS reset"
                )
            )
        }
    }
}
