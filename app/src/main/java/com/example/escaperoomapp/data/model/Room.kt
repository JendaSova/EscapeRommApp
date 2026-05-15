package com.example.escaperoomapp.data.model

data class Room(
    val id: String,
    val name: String,
    val description: String,
    val difficulty: String,
    val timeLimit: Int,
    val imageUrl: String,
    val youtubeUrl: String = ""
)
