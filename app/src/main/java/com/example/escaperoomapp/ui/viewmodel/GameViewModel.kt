package com.example.escaperoomapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escaperoomapp.data.model.Puzzle
import com.example.escaperoomapp.data.repository.FakePuzzleRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(private val puzzleRepository: FakePuzzleRepository) : ViewModel() {
    private val _puzzles = MutableStateFlow<List<Puzzle>>(emptyList())
    val puzzles: StateFlow<List<Puzzle>> = _puzzles
    private val _currentPuzzleIndex = MutableStateFlow(0)
    val currentPuzzleIndex: StateFlow<Int> = _currentPuzzleIndex
    private val _answerResult = MutableStateFlow<Boolean?>(null)
    val answerResult: StateFlow<Boolean?> = _answerResult
    private val _timeLeft = MutableStateFlow(0)
    val timeLeft: StateFlow<Int> = _timeLeft
    private var timerJob: Job? = null
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score
    private val _hintsLeft = MutableStateFlow(3)
    val hintsLeft: StateFlow<Int> = _hintsLeft
    private val _currentHint = MutableStateFlow<String?>(null)
    val currentHint: StateFlow<String?> = _currentHint
    private val _isGameFinished = MutableStateFlow(false)
    val isGameFinished: StateFlow<Boolean> = _isGameFinished
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun startGame(roomId: String, timeLimitMinutes: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _puzzles.value = puzzleRepository.getPuzzlesByRoom(roomId)
                _currentPuzzleIndex.value = 0
                _score.value = 0
                _hintsLeft.value = 3
                _isGameFinished.value = false
                startTimer(timeLimitMinutes * 60)
            } catch (e: Exception) {
                _isGameFinished.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitAnswer(answer: String) {
        val currentPuzzle = _puzzles.value.getOrNull(_currentPuzzleIndex.value) ?: return
        val isCorrect = answer.trim().equals(currentPuzzle.correctAnswer.trim(), ignoreCase = true)
        _answerResult.value = isCorrect
        if (isCorrect) {
            _score.value += 100 + _hintsLeft.value * 10
            viewModelScope.launch {
                delay(1000)
                moveToNextPuzzle()
            }
        }
    }

    fun useHint() {
        if (_hintsLeft.value > 0) {
            val currentPuzzle = _puzzles.value.getOrNull(_currentPuzzleIndex.value) ?: return
            _currentHint.value = currentPuzzle.hint
            _hintsLeft.value -= 1
        }
    }

    private fun moveToNextPuzzle() {
        _answerResult.value = null
        _currentHint.value = null
        if (_currentPuzzleIndex.value < _puzzles.value.size - 1) {
            _currentPuzzleIndex.value += 1
        } else { finishGame() }
    }

    private fun startTimer(totalSeconds: Int) {
        _timeLeft.value = totalSeconds
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1
            }
            finishGame()
        }
    }

    private fun finishGame() {
        timerJob?.cancel()
        _isGameFinished.value = true
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
