package com.fhanafi.cerdikia.ui.rangking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.repository.RangkingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RangkingViewModel(
    private val repository: RangkingRepository,
) : ViewModel() {

    private val _topPlayerRankingList = MutableStateFlow<List<RankingItem>>(emptyList())
    val topPlayerRankingList: StateFlow<List<RankingItem>> = _topPlayerRankingList
    val userData: Flow<UserModel> = repository.getUserData()

    fun fetchRankingForUser() {
        viewModelScope.launch {
            try {
                val user = repository.getUserData().first() // only take the current user once
                val kelasId = user.kelas
                val data = repository.fetchRanking(kelasId)
                Log.d("RankingPhoto", "Before insert: user photoUrl = ${user.photoUrl}")

                _topPlayerRankingList.value = data.map {
                    val isCurrentUser = it.nama == user.nama
                    val playerPhoto = if (isCurrentUser) user.photoUrl else null
                    Log.d("RankingPhoto", "Player photoUrl: $playerPhoto")
                    /* it still using photoUrl from data store to show only the current user photo.
                     If i want to show all photo user in rangking the backend was need to update so it can upload the photo or atleast has response that photo url included */
                    RankingItem(
                        rank = it.ranking ?: 0,
                        playerName = it.nama ?: "-",
                        xp = it.exp ?: 0,
                        isCurrentUser = isCurrentUser,
                        photoUrl = playerPhoto
                    )
                }
            } catch (e: Exception) {
                Log.e("RangkingViewModel", "Error fetching ranking: ${e.message}")
            }
        }
    }
}