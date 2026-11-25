package com.smokingquitsystem.app.domain

import kotlin.math.max

object QuitCalculations {

    private const val DAY_MS = 86_400_000L
    private const val HOUR_MS = 3_600_000L
    private const val MIN_MS = 60_000L

    fun elapsedMs(quitStartMillis: Long, nowMillis: Long): Long =
        max(0L, nowMillis - quitStartMillis)

    fun daysOnJourney(quitStartMillis: Long, nowMillis: Long): Long =
        elapsedMs(quitStartMillis, nowMillis) / DAY_MS

    fun cigsAvoided(
        quitStartMillis: Long,
        nowMillis: Long,
        cigsPerDay: Int,
        activeMode: String
    ): Int {
        val totalDays = (elapsedMs(quitStartMillis, nowMillis) / DAY_MS).toInt()
        val gradual = activeMode == "Gradual reduction"
        if (!gradual) return totalDays * cigsPerDay
        return (0 until totalDays).sumOf { day ->
            val reduction = ((day / 7) * 0.20).coerceIn(0.0, 1.0)
            (cigsPerDay * reduction).toInt()
        }
    }

    fun moneySaved(cigsAvoided: Int, packPrice: Float, packSize: Int): Int {
        val safePack = packSize.coerceAtLeast(1)
        return (cigsAvoided * (packPrice / safePack)).toInt()
    }

    fun formatDuration(quitStartMillis: Long, nowMillis: Long): String {
        val ms = elapsedMs(quitStartMillis, nowMillis)
        val d = ms / DAY_MS
        val h = (ms / HOUR_MS) % 24
        val m = (ms / MIN_MS) % 60
        return "${d}d  ${h}h  ${m}m"
    }

    fun gradualTargetPerDay(days: Long, baseCigsPerDay: Int, activeMode: String): Int {
        if (activeMode != "Gradual reduction") return 0
        val week = (days / 7).toInt()
        return max(0, (baseCigsPerDay * (1.0 - (week * 0.20))).toInt())
    }

    fun tr(lang: String, english: String, roman: String): String = when (lang) {
        "English" -> english
        "Roman Urdu" -> roman
        else -> "$english / $roman"
    }

    fun pageTitle(page: String, lang: String): String = tr(
        lang,
        page,
        when (page) {
            "Home" -> "Aapki progress"
            "Progress" -> "Taraqqi"
            "SOS" -> "Craving SOS"
            "Quotes" -> "Honsla afzai"
            else -> "Settings"
        }
    )
}
