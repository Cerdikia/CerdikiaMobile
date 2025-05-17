package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class ReedemGiftResponse(

	@field:SerializedName("diamond_balance")
	val diamondBalance: Int? = null,

	@field:SerializedName("total_diamond_cost")
	val totalDiamondCost: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null
)

data class ItemsItem(

	@field:SerializedName("kode_penukaran")
	val kodePenukaran: String? = null,

	@field:SerializedName("jumlah")
	val jumlah: Int? = null,

	@field:SerializedName("id_barang")
	val idBarang: Int? = null,

	@field:SerializedName("nama_barang")
	val namaBarang: String? = null,

	@field:SerializedName("diamond_cost")
	val diamondCost: Int? = null
)
