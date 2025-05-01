package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdatePointResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: UpdateUserProfileData? = null
)

data class UpdateUserProfileData(

	@field:SerializedName("diamond")
	val diamond: Int? = null,

	@field:SerializedName("exp")
	val exp: Int? = null
)
