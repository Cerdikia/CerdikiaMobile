//package com.fhanafi.cerdikia.ui.shop
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.fhanafi.cerdikia.data.pref.MisiHarianPreference
//
//class ShopViewModelFactory(
//    private val pref: MisiHarianPreference
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ShopViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return ShopViewModel(pref) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}