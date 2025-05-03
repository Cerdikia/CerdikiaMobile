package com.fhanafi.cerdikia.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mapel_items")
data class MapelEntity(
    @PrimaryKey val id_mapel: Int,
    val nama_mapel: String,
    val kelas: String,
    val jumlah_modul: Int,
    val finished: Boolean // Add this to support filtering locally
)