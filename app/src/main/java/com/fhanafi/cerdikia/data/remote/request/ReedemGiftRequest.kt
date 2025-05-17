package com.fhanafi.cerdikia.data.remote.request

import com.google.gson.annotations.SerializedName

data class ReedemGiftRequest(

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class ItemsItem(

	@field:SerializedName("jumlah")
	val jumlah: Int? = null,

	@field:SerializedName("id_barang")
	val idBarang: Int? = null
)
