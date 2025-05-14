package com.fhanafi.cerdikia.helper

import com.fhanafi.cerdikia.ui.shop.Toko

interface OnShopItemInteractionListener {
    fun onItemBought(item: Toko) // Called when the "Beli" button is clicked (first time)
    fun onItemCountChanged(item: Toko, quantity: Int) // Called when increment/decrement buttons are clicked
}