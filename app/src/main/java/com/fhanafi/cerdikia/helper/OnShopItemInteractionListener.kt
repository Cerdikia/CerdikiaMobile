package com.fhanafi.cerdikia.helper

import com.fhanafi.cerdikia.data.remote.response.HadiahDataItem

interface OnShopItemInteractionListener {
    fun onItemBought(item: HadiahDataItem) // Called when the "Beli" button is clicked (first time)
    fun onItemCountChanged(item: HadiahDataItem, quantity: Int) // Called when increment/decrement buttons are clicked
    fun onVerifikasiClicked()
}