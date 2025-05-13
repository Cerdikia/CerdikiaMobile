package com.fhanafi.cerdikia.helper

import android.os.CountDownTimer
import android.widget.TextView
import com.fhanafi.cerdikia.data.pref.MisiHarianData
import java.util.concurrent.TimeUnit

object DailyQuestUtils {

    fun timeUntilNextReset(lastResetTime: Long): Long {
        val now = System.currentTimeMillis()
        val nextReset = lastResetTime + 24 * 60 * 60 * 1000L
        return (nextReset - now).coerceAtLeast(0L)
    }

    fun formatMillisToHMS(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun startCountdownTimer(millisUntilFinished: Long, textView: TextView): CountDownTimer {
        return object : CountDownTimer(millisUntilFinished, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                textView.text = formatMillisToHMS(millisUntilFinished)
            }

            override fun onFinish() {
                textView.text = "00:00:00"
            }
        }.start()
    }
}
