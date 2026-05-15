package com.example.escaperoomapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escaperoomapp.data.model.GameResult
import com.example.escaperoomapp.data.repository.GameResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultViewModel(private val repository: GameResultRepository) : ViewModel() {

    private val _allResults = MutableStateFlow<List<GameResult>>(emptyList())
    val allResults: StateFlow<List<GameResult>> = _allResults

    init {
        viewModelScope.launch {
            repository.getAllResults().collect { results ->
                _allResults.value = results
            }
        }
    }

    fun saveResult(playerName: String, roomName: String, score: Int, timeUsed: Int, hintsUsed: Int) {
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val result = GameResult(
                playerName = playerName, roomName = roomName, score = score,
                timeUsed = timeUsed, hintsUsed = hintsUsed, date = dateFormat.format(Date())
            )
            repository.insertResult(result)
        }
    }

    fun deleteAllResults() {
        viewModelScope.launch { repository.deleteAllResults() }
    }
}
