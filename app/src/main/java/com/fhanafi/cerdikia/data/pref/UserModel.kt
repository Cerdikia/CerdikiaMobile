package com.fhanafi.cerdikia.data.pref

data class UserModel(
    val email: String = "",
//    val isLogin: Boolean,
    val nama: String = "",
    val kelas: Int = 0,
    val photoUrl: String? = null,
    val xp: Int = 0,
    val gems: Int = 0,
    val completedMateriIds: Set<Int> = emptySet(),
    val accessToken: String = "",
    val refreshToken: String = ""
)