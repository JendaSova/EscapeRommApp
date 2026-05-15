package com.example.escaperoomapp.data.model

data class GameResult(
    val id: Long = System.currentTimeMillis(),
    val playerName: String,
    val roomName: String,
    val score: Int,
    val timeUsed: Int,
    val hintsUsed: Int,
    val date: String
)
