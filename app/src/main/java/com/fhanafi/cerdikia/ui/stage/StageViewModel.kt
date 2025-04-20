package com.fhanafi.cerdikia.ui.stage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fhanafi.cerdikia.R

class StageViewModel : ViewModel() {
    
    private val _materiList = MutableLiveData<List<MateriItem>>()
    val materiList: LiveData<List<MateriItem>> = _materiList
    
    init {
        loadMateri()
    }
    //TODO: #1 harus memastikan si materi ada id untuk identifier entah di API atau di local
    //TODO: #2 icon drawable harusnya di set di local bukan melalui api karana bisa memberatkan komputasi api
    //TODO: #3 untuk soal yang sudah selesai icon dari materi berubah menjadi ic_finish dan jika belum menjadi ic_lock dan ic_unlock di mana progress player berada
    private fun loadMateri(){
        _materiList.value = listOf(
//            MateriItem("Materi 1 from VM", "Deskripsi dari ViewModel 1", R.drawable.ic_unlock, 1),
            MateriItem(1),
            MateriItem(2),
            MateriItem(3),
            MateriItem(4),
            MateriItem(5),
            MateriItem(6),
            MateriItem(7),
            MateriItem(8),
            MateriItem(9)
        )
    }

    fun updateMateriCompletion(materiId: Int) {
        _materiList.value = _materiList.value?.map {
            if (it.id == materiId) it.copy(isCompleted = true) else it
        }
    }
}