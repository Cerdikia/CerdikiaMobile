package com.fhanafi.cerdikia.ui.rangking

data class RankingItem (
    val rank: Int,
    val playerName: String,
    val xp: Int,
    val isCurrentUser: Boolean = false
)