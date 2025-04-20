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
            preferences[XP_KEY] = xp
            preferences[GEMS_KEY] = gems
        }
    }

    suspend fun addCompletedMateri(materiId: Int) {
        dataStore.edit { preferences ->
            val current = preferences[COMPLETED_MATERI_KEY]?.split(",")?.mapNotNull { it.toIntOrNull() }?.toMutableSet() ?: mutableSetOf()
            current.add(materiId)
            preferences[COMPLETED_MATERI_KEY] = current.joinToString(",")
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
