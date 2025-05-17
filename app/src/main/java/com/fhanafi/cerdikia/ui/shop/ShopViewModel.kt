package com.fhanafi.cerdikia.ui.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.cerdikia.data.pref.MisiHarianData
import com.fhanafi.cerdikia.data.pref.MisiHarianPreference
import com.fhanafi.cerdikia.data.remote.request.ReedemGiftRequest
import com.fhanafi.cerdikia.data.remote.response.EmailVerifResponse
import com.fhanafi.cerdikia.data.remote.response.HadiahDataItem
import com.fhanafi.cerdikia.data.remote.response.ReedemGiftResponse
import com.fhanafi.cerdikia.data.remote.response.VerifData
import com.fhanafi.cerdikia.data.repository.ShopRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class ShopViewModel(
    private val shopRepository: ShopRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val dailyQuest = shopRepository.questFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, shopRepository.getDefaultQuestData())

    val hadiahList: StateFlow<List<HadiahDataItem>> = shopRepository.gifts
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun loadHadiahList() {
        viewModelScope.launch {
            _isLoading.value = true
            try{
                delay(5000) // walaupun sudah terdelay karena ada observe daily quest local jadi bentrok loading state nya dan kemungkinan loading state hadiah true karena daily quest local telah tersajikan data local nya
                shopRepository.loadGifts()
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }
        }
    }

    private val _redeemResult = MutableStateFlow<Result<Pair<ReedemGiftResponse, String?>>?>(null)
    val redeemResult: StateFlow<Result<Pair<ReedemGiftResponse, String?>>?> = _redeemResult

    fun redeemGifts(items: List<ReedemItem>) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = shopRepository.redeemGifts(items)
            _redeemResult.value = result
            _isLoading.value = false
        }
    }

    private val _receiptFile = MutableStateFlow<Result<ResponseBody>?>(null)
    val receiptFile: StateFlow<Result<ResponseBody>?> = _receiptFile

    fun downloadReceipt(kodePenukaran: String) {
        viewModelScope.launch {
            val result = shopRepository.downloadReceipt(kodePenukaran)
            _receiptFile.value = result
        }
    }

    private val _verifData = MutableStateFlow<VerifData?>(null)
    val verifData: StateFlow<VerifData?> = _verifData

    fun fetchVerifiedStatus() {
        viewModelScope.launch {
            _verifData.value = shopRepository.getVerifiedStatus()
        }
    }

    private val _emailVerifResult = MutableStateFlow<Result<EmailVerifResponse>?>(null)
    val emailVerifResult: StateFlow<Result<EmailVerifResponse>?> = _emailVerifResult

    fun sendEmailVerification() {
        viewModelScope.launch {
            val result = shopRepository.sendEmailVerification()
            _emailVerifResult.value = result
        }
    }

    fun addXpEarned(xp: Int) = viewModelScope.launch {
        shopRepository.addXpEarned(xp)
    }

    fun incrementQuizCompleted() = viewModelScope.launch {
        shopRepository.incrementQuizCompleted()
    }

    fun checkAndResetQuestIfNeeded() = viewModelScope.launch {
        shopRepository.checkAndResetIfNeeded()
    }

    fun addStudyMinutes(minutes: Int) = viewModelScope.launch {
        shopRepository.addStudyMinutes(minutes)
    }

    fun setXpRewardClaimed(claimed: Boolean) = viewModelScope.launch {
        shopRepository.setXpRewardClaimed(claimed)
    }

    fun setQuizRewardClaimed(claimed: Boolean) = viewModelScope.launch {
        shopRepository.setQuizRewardClaimed(claimed)
    }

    fun setStudyTimeRewardClaimed(claimed: Boolean) = viewModelScope.launch {
        shopRepository.setStudyTimeRewardClaimed(claimed)
    }

}