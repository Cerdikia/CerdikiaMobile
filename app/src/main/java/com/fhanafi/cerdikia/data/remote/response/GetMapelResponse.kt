package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetMapelResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: List<MapelDataItem?>? = null
)

data class MapelDataItem(

	@field:SerializedName("kelas")
	val kelas: String? = null,

	@field:SerializedName("nama_mapel")
	val namaMapel: String? = null,

	@field:SerializedName("jumlah_modul")
	val jumlahModul: Int? = null,

	@field:SerializedName("id_mapel")
	val idMapel: Int? = null
)
