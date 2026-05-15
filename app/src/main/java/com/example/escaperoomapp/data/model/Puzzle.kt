package com.example.escaperoomapp.data.model

data class Puzzle(
    val id: String,
    val roomId: String,
    val question: String,
    val type: String,
    val optionsRaw: String?,
    val correctAnswer: String,
    val hint: String,
    val codeWord: String?
) {
    val options: List<String>?
        get() = if (optionsRaw.isNullOrEmpty()) null
                else optionsRaw.split(",").map { it.trim() }
}
