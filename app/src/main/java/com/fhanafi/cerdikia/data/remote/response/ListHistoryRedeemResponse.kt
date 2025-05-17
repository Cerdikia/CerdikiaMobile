package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListHistoryRedeemResponse(

	@field:SerializedName("data")
	val data: List<ListRedeemDataItem?>? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ListRedeemDataItem(

	@field:SerializedName("diamond")
	val diamond: Int? = null,

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("kode_penukaran")
	val kodePenukaran: String? = null,

	@field:SerializedName("jumlah")
	val jumlah: Int? = null,

	@field:SerializedName("nama_siswa")
	val namaSiswa: String? = null,

	@field:SerializedName("id_barang")
	val idBarang: Int? = null,

	@field:SerializedName("status_penukaran")
	val statusPenukaran: String? = null,

	@field:SerializedName("tanggal_penukaran")
	val tanggalPenukaran: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id_log")
	val idLog: Int? = null,

	@field:SerializedName("nama_barang")
	val namaBarang: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
