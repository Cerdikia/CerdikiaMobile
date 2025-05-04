package com.fhanafi.cerdikia.ui.question

data class Question(
    val questionText: String,
    val answerOptions: List<Pair<String, String>>,
    val correctAnswer: String
)
