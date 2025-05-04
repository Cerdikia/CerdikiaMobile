package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetMateriResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: List<MateriDataItem?>? = null
)

data class MateriDataItem(

	@field:SerializedName("id_module")
	val idModule: Int? = null,

	@field:SerializedName("module")
	val module: Int? = null,

	@field:SerializedName("module_judul")
	val moduleJudul: String? = null,

	@field:SerializedName("is_completed")
	val isCompleted: Boolean? = null,

	@field:SerializedName("module_deskripsi")
	val moduleDeskripsi: String? = null
)
