package com.example.escaperoomapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDetailScreen(
    roomId: String,
    roomName: String,
    timeLimit: Int,
    youtubeUrl: String,
    onStartGame: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = roomName, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Zpět")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)
            .verticalScroll(rememberScrollState())) {
            AsyncImage(model = "https://picsum.photos/seed/${roomId}/800/400",
                contentDescription = roomName,
                modifier = Modifier.fillMaxWidth().height(220.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)))
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = roomName, fontSize = 24.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp))
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRow(label = "⏱ Časový limit", value = "$timeLimit minut")
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow(label = "🔑 ID místnosti", value = roomId)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow(label = "💡 Nápovědy", value = "3 dostupné")
                    }
                }
                Text(text = "Jak hrát?", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp))
                listOf(
                    "🔍 Čtěte hádanky pozorně a přemýšlejte logicky.",
                    "💡 Zatřeste telefonem nebo klikněte pro nápovědu.",
                    "⏱ Sledujte čas - po vypršení limitu hra končí.",
                    "✅ Za každou správnou odpověď získáte body.",
                    "🏆 Snažte se dokončit místnost co nejrychleji!"
                ).forEach { rule ->
                    Text(text = rule, fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 6.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Tlačítko pro YouTube video nápovědu - konektivita k externí službě
                if (youtubeUrl.isNotEmpty()) {
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "▶ Sledovat video nápovědu na YouTube",
                            fontSize = 14.sp, color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(onClick = onStartGame, modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp)) {
                    Text(text = "🚀 Spustit hru", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}
