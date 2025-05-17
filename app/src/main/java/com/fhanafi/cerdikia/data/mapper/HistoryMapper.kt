package com.fhanafi.cerdikia.data.mapper

import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.data.remote.response.ListRedeemDataItem
import com.fhanafi.cerdikia.ui.history.HistoryItem

fun ListRedeemDataItem.toHistoryItem(): HistoryItem {
    return HistoryItem(
        transactionId = kodePenukaran ?: "-",
        itemName = namaBarang ?: "-",
        itemCount = jumlah ?: 0,
        status = statusPenukaran ?: "-",
        dateExchange = tanggalPenukaran ?: "-",
        imageResId = R.drawable.img_pencil // You can later map dynamically
    )
}