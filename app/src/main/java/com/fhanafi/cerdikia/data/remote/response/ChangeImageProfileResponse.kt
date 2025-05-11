package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class ChangeImageProfileResponse(

	@field:SerializedName("image_profile")
	val imageProfile: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
