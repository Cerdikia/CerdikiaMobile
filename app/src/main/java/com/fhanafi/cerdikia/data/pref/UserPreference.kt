package com.fhanafi.cerdikia.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUserData(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                email = preferences[EMAIL_KEY] ?: "",
                nama = preferences[NAMA_KEY] ?: "",
                kelas = preferences[KELAS_KEY] ?: 0,
                xp = preferences[XP_KEY] ?: 0,
                gems = preferences[GEMS_KEY] ?: 0,
                completedMateriIds = preferences[COMPLETED_MATERI_KEY]?.split(",")?.mapNotNull {
                    it.toIntOrNull()
                }?.toSet() ?: emptySet()
            )
        }
    }

    suspend fun updateXpAndGems(xp: Int, gems: Int) {
        dataStore.edit { preferences ->
            preferences[XP_KEY] = xp
            preferences[GEMS_KEY] = gems
        }
    }

    suspend fun updateUserData(xp: Int, gems: Int, completedId: Int){
        dataStore.edit { preferences ->
            val currentXp = preferences[XP_KEY] ?: 0
            val currentGems = preferences[GEMS_KEY] ?: 0

            preferences[XP_KEY] = currentXp + xp
            preferences[GEMS_KEY] = currentGems + gems

            val currentCompleted = preferences[COMPLETED_MATERI_KEY]
                ?.split(",")
                ?.mapNotNull { it.toIntOrNull() }
                ?.toMutableSet() ?: mutableSetOf()

            currentCompleted.add(completedId)
            preferences[COMPLETED_MATERI_KEY] = currentCompleted.joinToString(",")
        }
    }

    suspend fun addCompletedMateri(materiId: Int) {
        dataStore.edit { preferences ->
            val current = preferences[COMPLETED_MATERI_KEY]?.split(",")?.mapNotNull { it.toIntOrNull() }?.toMutableSet() ?: mutableSetOf()
            current.add(materiId)
            preferences[COMPLETED_MATERI_KEY] = current.joinToString(",")
        }
    }

    suspend fun saveNama(nama: String) {
        dataStore.edit { preferences ->
            preferences[NAMA_KEY] = nama
        }
    }

    suspend fun saveKelas(kelas: Int) {
        dataStore.edit { preferences ->
            preferences[KELAS_KEY] = kelas
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    suspend fun clearData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NAMA_KEY = stringPreferencesKey("nama")
        private val KELAS_KEY = intPreferencesKey("kelas")
        private val XP_KEY = intPreferencesKey("xp")
        private val GEMS_KEY = intPreferencesKey("gems")
        private val COMPLETED_MATERI_KEY = stringPreferencesKey("completed_materi_ids")

        fun getInstance(context: Context): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(context.dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
