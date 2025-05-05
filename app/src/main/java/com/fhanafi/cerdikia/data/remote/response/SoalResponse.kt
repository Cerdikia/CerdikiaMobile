package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class SoalResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: List<SoalDataItem?>? = null
)

data class SoalDataItem(

	@field:SerializedName("opsi_b")
	val opsiB: String? = null,

	@field:SerializedName("opsi_c")
	val opsiC: String? = null,

	@field:SerializedName("id_module")
	val idModule: Int? = null,

	@field:SerializedName("soal")
	val soal: String? = null,

	@field:SerializedName("opsi_a")
	val opsiA: String? = null,

	@field:SerializedName("opsi_d")
	val opsiD: String? = null,

	@field:SerializedName("id_soal")
	val idSoal: Int? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("jawaban")
	val jawaban: String? = null
)
