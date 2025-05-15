package com.fhanafi.cerdikia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // TODO: this not declare as player flag but it was an english course so in the future i will add some quiz subjects
    // so i think it has a wrong implementation and i will change it later
//    private val _playerFlag = MutableStateFlow(R.drawable.ic_indoflag)
//    val playerFlag: StateFlow<Int> = _playerFlag.asStateFlow()

    private val _selectedRoute = MutableStateFlow(R.id.navigation_home)
    val selectedRoute: StateFlow<Int> = _selectedRoute

    fun updateSelectedRoute(routeId: Int) {
        viewModelScope.launch {
            _selectedRoute.emit(routeId)
        }
    }
}