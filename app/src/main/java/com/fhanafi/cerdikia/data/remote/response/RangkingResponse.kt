package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class RangkingResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null
)

data class DataItem(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("kelas")
	val kelas: String? = null,

	@field:SerializedName("ranking")
	val ranking: Int? = null,

	@field:SerializedName("exp")
	val exp: Int? = null
)
