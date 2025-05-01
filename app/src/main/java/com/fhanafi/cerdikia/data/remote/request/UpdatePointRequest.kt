package com.fhanafi.cerdikia.data.remote.request

import com.google.gson.annotations.SerializedName

data class UpdatePointRequest(

	@field:SerializedName("diamond")
	val diamond: Int? = null,

	@field:SerializedName("exp")
	val exp: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
