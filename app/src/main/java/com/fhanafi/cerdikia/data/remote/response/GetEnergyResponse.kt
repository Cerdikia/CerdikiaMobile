package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetEnergyResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: EnergyData? = null
)

data class EnergyData(

	@field:SerializedName("Email")
	val email: String? = null,

	@field:SerializedName("Energy")
	val energy: Int? = null,

	@field:SerializedName("LastUpdated")
	val lastUpdated: String? = null
)
