package com.example.escaperoomapp.data.repository

import android.content.Context
import com.example.escaperoomapp.data.model.GameResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class GameResultRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("game_results", Context.MODE_PRIVATE)
    private val KEY_RESULTS = "results"
    private val gson = Gson()
    private val _results = MutableStateFlow<List<GameResult>>(loadResults())

    fun getAllResults(): Flow<List<GameResult>> = _results

    suspend fun insertResult(result: GameResult) {
        val currentResults = loadResults().toMutableList()
        currentResults.add(result)
        val sorted = currentResults.sortedByDescending { it.score }
        saveResults(sorted)
        _results.value = sorted
    }

    suspend fun deleteAllResults() {
        prefs.edit().remove(KEY_RESULTS).apply()
        _results.value = emptyList()
    }

    private fun loadResults(): List<GameResult> {
        val json = prefs.getString(KEY_RESULTS, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<GameResult>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveResults(results: List<GameResult>) {
        val json = gson.toJson(results)
        prefs.edit().putString(KEY_RESULTS, json).apply()
    }
}
