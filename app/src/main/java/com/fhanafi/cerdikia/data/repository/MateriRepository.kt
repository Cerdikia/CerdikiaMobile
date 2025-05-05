package com.fhanafi.cerdikia.data.repository

import com.fhanafi.cerdikia.data.remote.response.SoalDataItem
import com.fhanafi.cerdikia.data.remote.retrofit.ApiService
import com.fhanafi.cerdikia.ui.stage.MateriItem

class MateriRepository(private val apiService: ApiService) {

    suspend fun getMateriList(idMapel: Int, idKelas: Int, email: String): List<MateriItem> {
        val response = apiService.getMateri(
            idMapel = idMapel,
            idKelas = idKelas,
            finished = true, // fixed
            email = email
        )

        return response.data?.mapNotNull { item ->
            item?.let {
                MateriItem(
                    id = it.idModule ?: return@let null,
                    title = it.moduleJudul ?: "Untitled",
                    description = it.moduleDeskripsi ?: "",
                    isCompleted = it.isCompleted ?: false
                )
            }
        } ?: emptyList()
    }

    suspend fun getSoalByModule(idModule: Int): List<SoalDataItem> {
        val response = apiService.getSoal(idModule)
        return response.data?.filterNotNull() ?: emptyList()
    }

    companion object {
        @Volatile
        private var instance: MateriRepository? = null
        fun getInstance(apiService: ApiService): MateriRepository {
            return instance ?: synchronized(this) {
                MateriRepository(apiService).also { instance = it }
            }
        }
    }
}