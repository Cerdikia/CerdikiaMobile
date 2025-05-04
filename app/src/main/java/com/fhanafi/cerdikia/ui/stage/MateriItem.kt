package com.fhanafi.cerdikia.ui.stage

import com.google.gson.annotations.SerializedName

data class MateriItem(
    @SerializedName("id_module")
    val id: Int,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false
)
