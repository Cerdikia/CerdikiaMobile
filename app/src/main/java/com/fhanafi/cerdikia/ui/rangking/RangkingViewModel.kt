package com.fhanafi.cerdikia.ui.rangking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RangkingViewModel : ViewModel() {

    private val _topPlayerRankingList = MutableLiveData<List<RankingItem>>(
        listOf(
            RankingItem(1, "Player 1", 5000),
            RankingItem(2, "Player 2", 4009),
            RankingItem(3, "Player 3", 4007),
            RankingItem(4, "Player 4", 4004),
            RankingItem(5, "Player 5", 4002),
            RankingItem(6, "Player 6", 4001),
            RankingItem(7, "Player 7", 4000),
            RankingItem(8, "Player 8", 3999),
            RankingItem(9, "Player 9", 3998),
            RankingItem(10, "Player 10", 3997),
        )
    )

    val topPlayerRankingList: LiveData<List<RankingItem>> get() = _topPlayerRankingList
}