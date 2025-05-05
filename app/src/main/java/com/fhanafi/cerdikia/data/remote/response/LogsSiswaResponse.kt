package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class LogsSiswaResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: DataSiswa? = null
)

data class DataSiswa(

	@field:SerializedName("id_module")
	val idModule: Int? = null,

	@field:SerializedName("id_logs")
	val idLogs: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_kelas")
	val idKelas: Int? = null,

	@field:SerializedName("id_mapel")
	val idMapel: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("skor")
	val skor: Int? = null
)
