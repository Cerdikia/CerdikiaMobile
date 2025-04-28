package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: UserProfileData? = null
)

data class UserProfileData(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("date_created")
	val dateCreated: String? = null,

	@field:SerializedName("id_kelas")
	val idKelas: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
