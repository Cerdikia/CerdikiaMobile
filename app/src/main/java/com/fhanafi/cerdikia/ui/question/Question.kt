package com.fhanafi.cerdikia.ui.question

data class Question(
    val questionText: String,
    val answerOptions: List<String>,
    val correctAnswer: String
)
