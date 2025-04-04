package com.fhanafi.cerdikia.ui.components

data class BottomNavItem (
    val label: String,
    val icon: Int,
    val route: Int, // Menggunakan Int untuk ID resource jika tidak akan error tidak bisa menemukan navigation graph
    val isSelected: Boolean = false // for selected item
)