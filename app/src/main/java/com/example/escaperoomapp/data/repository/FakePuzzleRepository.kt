package com.example.escaperoomapp.data.repository

import com.example.escaperoomapp.data.fake.FakeDataSource
import com.example.escaperoomapp.data.model.Puzzle

class FakePuzzleRepository {
    suspend fun getPuzzlesByRoom(roomId: String): List<Puzzle> =
        FakeDataSource.puzzles.filter { it.roomId == roomId }
}
