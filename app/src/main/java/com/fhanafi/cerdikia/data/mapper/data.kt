package com.fhanafi.cerdikia.data.mapper

import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.remote.response.Data

fun Data.toUserModel(): UserModel {
    return UserModel(
        email = this.email ?: "",
        nama = this.nama ?: "",
        kelas = this.idKelas ?: 0,
        photoUrl = this.imageProfile,
        xp = 0, // default 0 saat login
        gems = 0, // default 0 saat login
        completedMateriIds = emptySet(), // default kosong saat login
        accessToken = this.accessToken ?: "",
        refreshToken = this.refreshToken ?: ""
    )
}
