package com.fhanafi.cerdikia.data.remote.request

import com.google.gson.annotations.SerializedName

data class EmailVerifRequest(

	@field:SerializedName("notifications")
	val notifications: List<NotificationsItem?>? = null
)

data class NotificationsItem(

	@field:SerializedName("form")
	val form: String? = null,

	@field:SerializedName("subject")
	val subject: String? = null,

	@field:SerializedName("dest")
	val dest: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("entity")
	val entity: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
