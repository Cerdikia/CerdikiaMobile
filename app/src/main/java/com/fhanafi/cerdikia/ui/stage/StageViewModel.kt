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
            MateriItem("Materi 1 from VM", "Deskripsi dari ViewModel 1", R.drawable.ic_unlock, 1),
            MateriItem("Materi 2 from VM", "Deskripsi dari ViewModel 2", R.drawable.ic_lock, 2),
            MateriItem("Materi 3 from VM", "Deskripsi dari ViewModel 3", R.drawable.ic_lock, 3),
            MateriItem("Materi 4 from VM", "Deskripsi dari ViewModel 4", R.drawable.ic_lock, 4),
            MateriItem("Materi 5 from VM", "Deskripsi dari ViewModel 5", R.drawable.ic_lock, 5),
            MateriItem("Materi 6 from VM", "Deskripsi dari ViewModel 6", R.drawable.ic_lock, 6),
            MateriItem("Materi 7 from VM", "Deskripsi dari ViewModel 7", R.drawable.ic_lock, 7),
            MateriItem("Materi 8 from VM", "Deskripsi dari ViewModel 8", R.drawable.ic_lock, 8),
            MateriItem("Materi 9 from VM", "Deskripsi dari ViewModel 9", R.drawable.ic_lock, 9),
            MateriItem("Materi 10 from VM", "Deskripsi dari ViewModel 10", R.drawable.ic_lock, 10)
        )
    }
}