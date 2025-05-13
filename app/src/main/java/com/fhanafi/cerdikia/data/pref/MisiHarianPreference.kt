package com.fhanafi.cerdikia.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import kotlinx.coroutines.flow.Flow
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile


class MisiHarianPreference(private val context: Context) {

    private val dataStore: DataStore<MisiHarianData> =
        DataStoreFactory.create(
            serializer = MisiHarianDataSerializer,
            produceFile = { context.dataStoreFile("daily_quest.pb") }
        )

    val questFlow: Flow<MisiHarianData> = dataStore.data

    suspend fun addXpEarned(amount: Int) {
        dataStore.updateData { quest ->
            quest.toBuilder()
                .setXpEarned(quest.xpEarned + amount)
                .build()
        }
    }

    suspend fun incrementQuizCompleted() {
        dataStore.updateData { quest ->
            quest.toBuilder()
                .setQuizzesCompleted(quest.quizzesCompleted + 1)
                .build()
        }
    }

//    suspend fun resetQuests() {
//        val now = System.currentTimeMillis()
//        dataStore.updateData {
//            it.toBuilder()
//                .clearXpEarned()
//                .clearQuizzesCompleted()
//                .clearMinutesStudied()
//                .setLastResetTime(now)
//                .build()
//        }
//    }

    suspend fun setXpRewardClaimed(claimed: Boolean) {
        dataStore.updateData {
            it.toBuilder().setXpRewardClaimed(claimed).build()
        }
    }

    suspend fun setQuizRewardClaimed(claimed: Boolean) {
        dataStore.updateData {
            it.toBuilder().setQuizRewardClaimed(claimed).build()
        }
    }

    suspend fun setStudyTimeRewardClaimed(claimed: Boolean) {
        dataStore.updateData {
            it.toBuilder().setStudyTimeRewardClaimed(claimed).build()
        }
    }

    suspend fun checkAndResetIfNeeded() {
        val now = System.currentTimeMillis()
        dataStore.updateData { quest ->
            val lastReset = quest.lastResetTime
            val elapsed = now - lastReset
            if (elapsed >= 24 * 60 * 60 * 1000L) {
                quest.toBuilder()
                    .clearXpEarned()
                    .clearQuizzesCompleted()
                    .clearMinutesStudied()
                    .setXpRewardClaimed(false)
                    .setQuizRewardClaimed(false)
                    .setStudyTimeRewardClaimed(false)
                    .setLastResetTime(now)
                    .build()
            } else {
                quest
            }
        }
    }

    suspend fun addStudyMinutes(minutes: Int) {
        dataStore.updateData { quest ->
            quest.toBuilder()
                .setMinutesStudied(quest.minutesStudied + minutes)
                .build()
        }
    }
}
