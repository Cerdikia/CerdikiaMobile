package com.fhanafi.cerdikia.data.remote.request

import com.google.gson.annotations.SerializedName

data class LogsSiswaRequest(

	@field:SerializedName("id_module")
	val idModule: Int? = null,

	@field:SerializedName("id_kelas")
	val idKelas: Int? = null,

	@field:SerializedName("id_mapel")
	val idMapel: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("skor")
	val skor: Int? = null
)
