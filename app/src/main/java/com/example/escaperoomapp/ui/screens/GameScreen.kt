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
import com.example.escaperoomapp.data.model.Puzzle
import com.example.escaperoomapp.ui.viewmodel.GameViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameScreen(
    roomId: String, timeLimit: Int,
    onShakeCallback: (() -> Unit) -> Unit,
    onGameFinished: (score: Int, timeUsed: Int, hintsUsed: Int) -> Unit
) {
    val viewModel: GameViewModel = koinViewModel()
    val puzzles by viewModel.puzzles.collectAsState()
    val currentPuzzleIndex by viewModel.currentPuzzleIndex.collectAsState()
    val answerResult by viewModel.answerResult.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val score by viewModel.score.collectAsState()
    val hintsLeft by viewModel.hintsLeft.collectAsState()
    val currentHint by viewModel.currentHint.collectAsState()
    val isGameFinished by viewModel.isGameFinished.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(roomId) { viewModel.startGame(roomId, timeLimit) }

    SideEffect { onShakeCallback { viewModel.useHint() } }

    LaunchedEffect(isGameFinished) {
        if (isGameFinished) {
            onGameFinished(score, (timeLimit * 60) - timeLeft, 3 - hintsLeft)
        }
    }

    when {
        isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        puzzles.isNotEmpty() -> {
            val currentPuzzle = puzzles[currentPuzzleIndex]
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                GameTopBar(timeLeft = timeLeft, score = score,
                    currentIndex = currentPuzzleIndex, totalPuzzles = puzzles.size)
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { (currentPuzzleIndex + 1).toFloat() / puzzles.size },
                    modifier = Modifier.fillMaxWidth().height(8.dp))
                Spacer(modifier = Modifier.height(24.dp))
                when (currentPuzzle.type) {
                    "choice" -> ChoicePuzzle(puzzle = currentPuzzle, answerResult = answerResult,
                        onAnswerSelected = { viewModel.submitAnswer(it) })
                    "code" -> CodePuzzle(puzzle = currentPuzzle, answerResult = answerResult,
                        onCodeSubmitted = { viewModel.submitAnswer(it) })
                }
                Spacer(modifier = Modifier.weight(1f))
                currentHint?.let { hint -> HintCard(hint = hint); Spacer(modifier = Modifier.height(8.dp)) }
                OutlinedButton(onClick = { viewModel.useHint() }, enabled = hintsLeft > 0,
                    modifier = Modifier.fillMaxWidth()) {
                    Text(text = if (hintsLeft > 0) "💡 Zatřes telefonem nebo klikni ($hintsLeft zbývá)"
                    else "💡 Žádné nápovědy nezbyly", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun GameTopBar(timeLeft: Int, score: Int, currentIndex: Int, totalPuzzles: Int) {
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timerColor = if (timeLeft < 60) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "⏱ %02d:%02d".format(minutes, seconds), fontSize = 20.sp,
            fontWeight = FontWeight.Bold, color = timerColor)
        Text(text = "${currentIndex + 1} / $totalPuzzles", fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "🏆 $score", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
fun ChoicePuzzle(puzzle: Puzzle, answerResult: Boolean?, onAnswerSelected: (String) -> Unit) {
    Column {
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Text(text = puzzle.question, fontSize = 18.sp, fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        puzzle.options?.forEach { option ->
            Button(onClick = { onAnswerSelected(option) }, enabled = answerResult != true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                shape = RoundedCornerShape(10.dp)) {
                Text(text = option, fontSize = 15.sp)
            }
        }
        answerResult?.let { isCorrect ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = if (isCorrect) "✅ Správně! Postupujete dál..." else "❌ Špatně, zkuste to znovu.",
                color = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun CodePuzzle(puzzle: Puzzle, answerResult: Boolean?, onCodeSubmitted: (String) -> Unit) {
    var codeInput by remember { mutableStateOf("") }
    Column {
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = puzzle.question, fontSize = 18.sp, fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center)
                puzzle.codeWord?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "🔑 Klíčové slovo: $it", fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = codeInput, onValueChange = { codeInput = it },
            label = { Text("Zadejte kód nebo heslo") }, enabled = answerResult != true,
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), singleLine = true)
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { onCodeSubmitted(codeInput) },
            enabled = codeInput.isNotBlank() && answerResult != true,
            modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(10.dp)) {
            Text(text = "🔓 Odemknout", fontSize = 15.sp)
        }
        answerResult?.let { isCorrect ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = if (isCorrect) "✅ Správný kód! Postupujete dál..." else "❌ Špatný kód, zkuste to znovu.",
                color = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun HintCard(hint: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "💡 ", fontSize = 20.sp)
            Text(text = hint, fontSize = 14.sp, color = MaterialTheme.colorScheme.onTertiaryContainer)
        }
    }
}
