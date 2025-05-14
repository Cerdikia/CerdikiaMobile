package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListHadiahResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: List<HadiahDataItem?>? = null
)

data class HadiahDataItem(

	@field:SerializedName("diamond")
	val diamond: Int? = null,

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("jumlah")
	val jumlah: Int? = null,

	@field:SerializedName("id_barang")
	val idBarang: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("nama_barang")
	val namaBarang: String? = null
)
