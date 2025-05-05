package com.fhanafi.cerdikia.ui.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.cerdikia.data.remote.response.SoalDataItem
import com.fhanafi.cerdikia.data.repository.MateriRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.fhanafi.cerdikia.helper.stripHtmlTags
import kotlinx.coroutines.delay

class SoalViewModel(private val repository: MateriRepository) : ViewModel() {

    private val _questionList = MutableStateFlow<List<Question>>(emptyList())
    val questionList: StateFlow<List<Question>> = _questionList.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    val currentQuestion: StateFlow<Question?> = _currentQuestionIndex
        .combine(_questionList) { index, list ->
            list.getOrNull(index)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _answerSelected = MutableStateFlow<String?>(null)
    val answerSelected: StateFlow<String?> = _answerSelected.asStateFlow()

    private val _isCorrectAnswer = MutableStateFlow<Boolean?>(null)
    val isCorrectAnswer: StateFlow<Boolean?> = _isCorrectAnswer.asStateFlow()

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished: StateFlow<Boolean> = _isQuizFinished.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var _correctAnswers = 0
    val correctAnswers: Int
        get() = _correctAnswers

    fun fetchQuestions(idModule: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                delay(3000) // biar ada effect loading
                val soalList = repository.getSoalByModule(idModule)
                val mapped = soalList.map { it.toQuestion() }
                _questionList.value = mapped
                _currentQuestionIndex.value = 0
                _correctAnswers = 0
                _answerSelected.value = null
                _isCorrectAnswer.value = null
                _isQuizFinished.value = false
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectAnswer(answer: String) {
        _answerSelected.value = answer
        val correct = currentQuestion.value?.correctAnswer == answer
        _isCorrectAnswer.value = correct
        if (correct) _correctAnswers++
    }

    fun nextQuestion() {
        val nextIndex = _currentQuestionIndex.value + 1
        if (nextIndex < _questionList.value.size) {
            _currentQuestionIndex.value = nextIndex
            _answerSelected.value = null
            _isCorrectAnswer.value = null
        } else {
            _isQuizFinished.value = true
        }
    }

    fun resetQuiz() {
        _currentQuestionIndex.value = 0
        _correctAnswers = 0
        _answerSelected.value = null
        _isCorrectAnswer.value = null
        _isQuizFinished.value = false
    }

    private fun SoalDataItem.toQuestion(): Question {
        return Question(
            questionText = soal ?: "",
            answerOptions = listOfNotNull(
                opsiA?.let { "a" to it },
                opsiB?.let { "b" to it },
                opsiC?.let { "c" to it },
                opsiD?.let { "d" to it }
            ),
            correctAnswer = jawaban?.lowercase() ?: ""
        )
    }
}