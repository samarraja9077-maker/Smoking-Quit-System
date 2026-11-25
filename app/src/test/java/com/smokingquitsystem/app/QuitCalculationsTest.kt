package com.smokingquitsystem.app

import com.smokingquitsystem.app.domain.QuitCalculations
import org.junit.Assert.assertEquals
import org.junit.Test

class QuitCalculationsTest {

    private val dayMs = 86_400_000L

    @Test
    fun immediateMode_calculatesAvoidedAndSavedCorrectly() {
        val start = 1_000_000L
        val now = start + (3 * dayMs) + 3_600_000L
        assertEquals(3L, QuitCalculations.daysOnJourney(start, now))

        val cigs = QuitCalculations.cigsAvoided(start, now, 10, "Quit immediately")
        assertEquals(30, cigs)

        val saved = QuitCalculations.moneySaved(cigs, 500f, 20)
        assertEquals(750, saved)
    }

    @Test
    fun gradualMode_calculatesWeeklyReduction() {
        val start = 0L
        val now = 14 * dayMs // 7 days @ 0% reduction + 7 days @ 20% reduction
        val cigs = QuitCalculations.cigsAvoided(start, now, 10, "Gradual reduction")
        assertEquals(14, cigs)
        assertEquals(6, QuitCalculations.gradualTargetPerDay(14L, 10, "Gradual reduction"))

        val day40 = 40 * dayMs // 0 + 14 + 28 + 42 + 56 + 50 = 190
        assertEquals(190, QuitCalculations.cigsAvoided(start, day40, 10, "Gradual reduction"))
    }

    @Test
    fun durationFormatting_matchesExpectedFormat() {
        val start = 0L
        val now = (2 * dayMs) + (5 * 3_600_000L) + (9 * 60_000L)
        assertEquals("2d  5h  9m", QuitCalculations.formatDuration(start, now))
    }

    @Test
    fun translations_respectSelectedLanguage() {
        assertEquals("Home", QuitCalculations.tr("English", "Home", "Aapki progress"))
        assertEquals("Aapki progress", QuitCalculations.tr("Roman Urdu", "Home", "Aapki progress"))
        assertEquals("Home / Aapki progress", QuitCalculations.tr("Both", "Home", "Aapki progress"))
    }
}
