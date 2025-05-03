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

    @Query("SELECT * FROM mapel_items WHERE finished = :finished")
    suspend fun getMapelsByFinished(finished: Boolean): List<MapelEntity>

    @Query("SELECT * FROM mapel_items WHERE finished = :finished")
    fun getMapelsByFinishedFlow(finished: Boolean): Flow<List<MapelEntity>>
}