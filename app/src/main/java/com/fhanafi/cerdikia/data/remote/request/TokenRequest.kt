package com.fhanafi.cerdikia.data.remote.request

import com.google.gson.annotations.SerializedName

data class TokenRequest(

	@field:SerializedName("refresh_token")
	val refreshToken: String? = null
)
