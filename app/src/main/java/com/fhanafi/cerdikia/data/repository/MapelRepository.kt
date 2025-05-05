package com.fhanafi.cerdikia.data.repository

import com.fhanafi.cerdikia.data.database.MapelDao
import com.fhanafi.cerdikia.data.database.MapelEntity
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class MapelRepository(private val apiService: ApiService, private val mapelDao: MapelDao) {

    suspend fun fetchMapel(idKelas: Int, finished: Boolean): List<MapelEntity> {
        return try {
            val response = apiService.getMapel(idKelas, finished)
            val mapelList = response.data?.filterNotNull()?.map {
                MapelEntity(
                    id_mapel = it.idMapel ?: 0,
                    nama_mapel = it.namaMapel ?: "",
                    kelas = it.kelas ?: "",
                    jumlah_modul = it.jumlahModul ?: 0,
                    finished = finished
                )
            } ?: emptyList()
            mapelDao.insertMapels(mapelList)
            mapelList
        } catch (e: Exception) {
            // fallback to local DB if network fails
            mapelDao.getMapelsByFinishedAndKelas(finished, idKelas.toString())
        }
    }

    fun getLocalMapels(idKelas: Int, finished: Boolean): Flow<List<MapelEntity>> {
        return mapelDao.getMapelsByFinishedAndKelasFlow(finished, idKelas.toString())
    }

    companion object {
        @Volatile
        private var INSTANCE: MapelRepository? = null

        fun getInstance(apiService: ApiService, mapelDao: MapelDao): MapelRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = MapelRepository(apiService, mapelDao)
                INSTANCE = instance
                instance
            }
        }
    }
}
