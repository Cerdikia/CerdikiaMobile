package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class CekVerifResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: VerifData? = null
)

data class VerifData(

	@field:SerializedName("verified_status")
	val verifiedStatus: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
