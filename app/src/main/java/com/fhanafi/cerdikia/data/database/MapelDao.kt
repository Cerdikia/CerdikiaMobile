package com.fhanafi.cerdikia.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MapelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMapels(mapels: List<MapelEntity>)

    @Query("SELECT * FROM mapel_items WHERE finished = :finished AND kelas = :kelas")
    fun getMapelsByFinishedAndKelasFlow(finished: Boolean, kelas: String): Flow<List<MapelEntity>>

    @Query("SELECT * FROM mapel_items WHERE finished = :finished AND kelas = :kelas")
    suspend fun getMapelsByFinishedAndKelas(finished: Boolean, kelas: String): List<MapelEntity>

}