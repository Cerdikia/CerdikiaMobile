package com.fhanafi.cerdikia.ui.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.cerdikia.data.pref.MisiHarianData
import com.fhanafi.cerdikia.data.pref.MisiHarianPreference
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShopViewModel(
    private val pref: MisiHarianPreference
) : ViewModel() {
    val dailyQuest = pref.questFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, MisiHarianData.getDefaultInstance())

    fun addXpEarned(xp: Int) = viewModelScope.launch { pref.addXpEarned(xp) }
    fun incrementQuizCompleted() = viewModelScope.launch { pref.incrementQuizCompleted() }

//    fun resetDailyQuest() {
//        viewModelScope.launch {
//            pref.resetQuests()
//        }
//    }

    fun checkAndResetQuestIfNeeded() = viewModelScope.launch {
        pref.checkAndResetIfNeeded()
    }

    fun addStudyMinutes(minutes: Int) = viewModelScope.launch {
        pref.addStudyMinutes(minutes)
    }

    fun setXpRewardClaimed(claimed: Boolean) = viewModelScope.launch {
        pref.setXpRewardClaimed(claimed)
    }

    fun setQuizRewardClaimed(claimed: Boolean) = viewModelScope.launch {
        pref.setQuizRewardClaimed(claimed)
    }

    fun setStudyTimeRewardClaimed(claimed: Boolean) = viewModelScope.launch {
        pref.setStudyTimeRewardClaimed(claimed)
    }

}