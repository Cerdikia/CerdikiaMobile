package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetPointResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: UserDataPoint? = null
)

data class UserDataPoint(

	@field:SerializedName("diamond")
	val diamond: Int? = null,

	@field:SerializedName("exp")
	val exp: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
