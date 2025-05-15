package com.fhanafi.cerdikia.helper

import android.util.Log
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun calculateRemainingTimeMillis(lastUpdated: String?): Long {
    if (lastUpdated == null) return 10 * 60 * 1000L // default 10 mins

    return try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val updatedTime = OffsetDateTime.parse(lastUpdated, formatter)
        val nextEnergyTime = updatedTime.plusMinutes(10)
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val duration = Duration.between(now, nextEnergyTime)
        maxOf(duration.toMillis(), 0)
    } catch (e: Exception) {
        Log.e("StageFragment", "Failed to parse time: ${e.message}")
        10 * 60 * 1000L // fallback to 10 mins
    }
}
