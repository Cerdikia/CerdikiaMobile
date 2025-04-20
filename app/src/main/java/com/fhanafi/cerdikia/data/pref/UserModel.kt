package com.fhanafi.cerdikia.data.pref

data class UserModel(
//    val email: String,
//    val token: String,
//    val refreshToken: String,
//    val isLogin: Boolean,
//    val username: String,
//    val password: String,
//    val photoUrl: String? = null,
//    val phone: String? = null,
    val xp: Int = 0,
    val gems: Int = 0,
    val completedMateriIds: Set<Int> = emptySet()
)