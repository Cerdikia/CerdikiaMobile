package com.fhanafi.cerdikia.data.remote.response

import com.google.gson.annotations.SerializedName

data class EmailVerifResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val data: List<EmailDataItem?>? = null
)

data class EmailDataItem(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("Entity")
	val entity: String? = null,

	@field:SerializedName("Dest")
	val dest: String? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Form")
	val form: String? = null,

	@field:SerializedName("CreatedAt")
	val createdAt: String? = null,

	@field:SerializedName("IDMessage")
	val iDMessage: Int? = null,

	@field:SerializedName("UpdatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("Subject")
	val subject: String? = null
)
