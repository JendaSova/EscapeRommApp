package com.example.escaperoomapp.data.repository

import com.example.escaperoomapp.data.fake.FakeDataSource
import com.example.escaperoomapp.data.model.Room

class FakeRoomRepository {
    suspend fun getRooms(): List<Room> = FakeDataSource.rooms
}
