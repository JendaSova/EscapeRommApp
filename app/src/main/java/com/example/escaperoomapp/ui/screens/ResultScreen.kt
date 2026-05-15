package com.example.escaperoomapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.escaperoomapp.ui.viewmodel.ResultViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResultScreen(
    score: Int, timeUsed: Int, hintsUsed: Int, roomName: String,
    onPlayAgain: () -> Unit, onLeaderboard: () -> Unit
) {
    val viewModel: ResultViewModel = koinViewModel()
    var showDialog by remember { mutableStateOf(true) }
    var playerName by remember { mutableStateOf("") }

    val rating = when {
        score >= 300 -> "🏆 Mistr únikových místností!"
        score >= 200 -> "⭐ Skvělý výkon!"
        score >= 100 -> "👍 Dobrá práce!"
        else -> "💪 Příště to zvládnete lépe!"
    }
    val timeFormatted = "%02d:%02d".format(timeUsed / 60, timeUsed % 60)

    if (showDialog) {
        Dialog(onDismissRequest = {}) {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🎉 Hra dokončena!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Zadejte své jméno pro uložení výsledku", fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = playerName, onValueChange = { playerName = it },
                        label = { Text("Vaše jméno") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (playerName.isNotBlank()) {
                                viewModel.saveResult(playerName, roomName, score, timeUsed, hintsUsed)
                                showDialog = false
                            }
                        },
                        enabled = playerName.isNotBlank(), modifier = Modifier.fillMaxWidth()
                    ) { Text("Uložit výsledek") }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Hra dokončena!", fontSize = 28.sp, fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = rating, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ResultRow("🏆 Celkové skóre", "$score bodů", true)
                Divider()
                ResultRow("⏱ Strávený čas", timeFormatted)
                Divider()
                ResultRow("💡 Použité nápovědy", "$hintsUsed / 3")
                Divider()
                ResultRow("✨ Bonus za nápovědy", "+ ${(3 - hintsUsed) * 10} bodů")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onLeaderboard, modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp)) {
            Text(text = "🏆 Zobrazit žebříček", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onPlayAgain, modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp)) {
            Text(text = "🔐 Hrát znovu", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ResultRow(label: String, value: String, isHighlighted: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = if (isHighlighted) 20.sp else 15.sp,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isHighlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
    }
}
