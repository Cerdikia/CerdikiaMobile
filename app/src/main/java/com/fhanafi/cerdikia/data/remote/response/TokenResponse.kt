package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: TokenData? = null
)

data class TokenData(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("refresh_token")
	val refreshToken: String? = null
)
