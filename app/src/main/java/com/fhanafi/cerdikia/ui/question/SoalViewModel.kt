package com.fhanafi.cerdikia.ui.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SoalViewModel : ViewModel() {
    private val _currentQuestion = MutableLiveData<Question>()
    val currentQuestion: LiveData<Question> = _currentQuestion

    private val _questionList = MutableLiveData<List<Question>>()
    val questionList: LiveData<List<Question>> = _questionList

    private val _currentQuestionIndex = MutableLiveData<Int>()
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _answerSelected = MutableLiveData<String?>()
    val answerSelected: LiveData<String?> = _answerSelected

    private val _isCorrectAnswer = MutableLiveData<Boolean?>()
    val isCorrectAnswer: LiveData<Boolean?> = _isCorrectAnswer

    private val _isQuizFinished = MutableLiveData<Boolean>()
    val isQuizFinished: LiveData<Boolean> = _isQuizFinished

    private var _correctAnswers = 0
    val correctAnswers: Int
        get() = _correctAnswers

    init {
        // Mock data in the ViewModel
        _questionList.value = listOf(
            Question("Apa warna langit?", listOf("Merah", "Biru", "Hijau"), "Biru"),
            Question("Ibukota Indonesia?", listOf("Jakarta", "Surabaya", "Bandung"), "Jakarta"),
            // Add more questions here
            Question("2 + 2 = ?", listOf("3", "4", "5"), "4"),
            Question(
                "Siapa presiden pertama RI?",
                listOf("Soekarno", "Soeharto", "Habibie"),
                "Soekarno"
            ),
            Question("Lambang negara Indonesia?", listOf("Garuda", "Komodo", "Pancasila"), "Garuda")
        )
        _currentQuestionIndex.value = 0
        _currentQuestion.value = _questionList.value?.get(_currentQuestionIndex.value ?: 0)
        _answerSelected.value = null
        _isCorrectAnswer.value = null
    }

    fun selectAnswer(answer: String) {
        _answerSelected.value = answer
        val isCorrect = _currentQuestion.value?.correctAnswer == answer
        _isCorrectAnswer.value = isCorrect
        if (isCorrect) {
            _correctAnswers++
        }
    }

    fun nextQuestion() {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val nextIndex = currentIndex + 1
        if (nextIndex < (_questionList.value?.size ?: 0)) {
            _currentQuestionIndex.value = nextIndex
            _currentQuestion.value = _questionList.value?.get(nextIndex)
            _answerSelected.value = null
            _isCorrectAnswer.value = null
        } else {
            _isQuizFinished.value = true
        }
    }

    fun resetQuizFinished() {
        _isQuizFinished.value = false
    }

}