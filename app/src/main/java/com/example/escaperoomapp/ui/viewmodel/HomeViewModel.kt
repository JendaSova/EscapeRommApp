package com.example.escaperoomapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escaperoomapp.data.model.Room
import com.example.escaperoomapp.data.repository.FakeRoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val roomRepository: FakeRoomRepository) : ViewModel() {
    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init { loadRooms() }

    fun loadRooms() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _rooms.value = roomRepository.getRooms()
            } catch (e: Exception) {
                _error.value = "Chyba: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
