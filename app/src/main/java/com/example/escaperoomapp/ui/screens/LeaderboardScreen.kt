package com.example.escaperoomapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.escaperoomapp.data.model.GameResult
import com.example.escaperoomapp.ui.viewmodel.ResultViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(onBack: () -> Unit) {
    val viewModel: ResultViewModel = koinViewModel()
    val results by viewModel.allResults.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏆 Žebříček", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.deleteAllResults() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Smazat vše")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (results.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🎮", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Zatím žádné výsledky", fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "Zahrajte si první hru!", fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(results) { index, result ->
                    LeaderboardItem(position = index + 1, result = result)
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(position: Int, result: GameResult) {
    val positionIcon = when (position) { 1 -> "🥇"; 2 -> "🥈"; 3 -> "🥉"; else -> "$position." }
    val timeFormatted = "%02d:%02d".format(result.timeUsed / 60, result.timeUsed % 60)

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (position <= 3) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = positionIcon, fontSize = 24.sp, modifier = Modifier.width(48.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = result.playerName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "${result.roomName} • ${result.date}", fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "⏱ $timeFormatted • 💡 ${result.hintsUsed}/3", fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(text = "${result.score}", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary)
        }
    }
}
