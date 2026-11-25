package com.smokingquitsystem.app.data

import android.content.Context
import androidx.compose.runtime.Immutable
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Immutable
@Entity(tableName = "quit_profile")
data class QuitProfileEntity(
    @PrimaryKey val id: Int = 1,
    val configured: Boolean = false,
    val quitStartMillis: Long = System.currentTimeMillis(),
    val cigsPerDay: Int = 10,
    val packPrice: Float = 500f,
    val packSize: Int = 20,
    val method: String = "Both",
    val activeMode: String = "Quit immediately",
    val language: String = "Both",
    val remindersEnabled: Boolean = true
)

@Immutable
@Entity(tableName = "craving_logs")
data class CravingLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestampMillis: Long = System.currentTimeMillis(),
    val type: String = "sos_completed",
    val note: String = ""
)

@Dao
interface QuitDao {
    @Query("SELECT * FROM quit_profile WHERE id = 1")
    fun observeProfile(): Flow<QuitProfileEntity?>

    @Query("SELECT * FROM quit_profile WHERE id = 1")
    suspend fun getProfileOnce(): QuitProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: QuitProfileEntity)

    @Query("SELECT COUNT(*) FROM craving_logs")
    fun observeCravingLogCount(): Flow<Int>

    @Insert
    suspend fun insertCravingLog(log: CravingLogEntity)
}

@Database(
    entities = [QuitProfileEntity::class, CravingLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class QuitDatabase : RoomDatabase() {
    abstract fun quitDao(): QuitDao

    companion object {
        @Volatile
        private var INSTANCE: QuitDatabase? = null

        fun getInstance(context: Context): QuitDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuitDatabase::class.java,
                    "smoking_quit_system.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

object ReminderPrefsSync {
    private const val PREF_NAME = "quit_system_boot"
    private const val KEY_REMINDERS = "reminders"
    private const val KEY_LANGUAGE = "language"

    fun sync(context: Context, remindersEnabled: Boolean, language: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_REMINDERS, remindersEnabled)
            .putString(KEY_LANGUAGE, language)
            .apply()
    }

    fun isRemindersEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_REMINDERS, true)

    fun getLanguage(context: Context): String =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, "Both") ?: "Both"
}
