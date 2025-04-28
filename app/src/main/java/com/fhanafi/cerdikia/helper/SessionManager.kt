package com.fhanafi.cerdikia.helper

import android.content.Context
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.data.pref.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SessionManager {
    fun clearSession(context: Context) {
        val userPreference = UserPreference.getInstance(context.dataStore)
        CoroutineScope(Dispatchers.IO).launch {
            userPreference.clearData()
        }
    }
}
