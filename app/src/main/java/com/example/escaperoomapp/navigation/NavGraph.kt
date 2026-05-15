package com.example.escaperoomapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.escaperoomapp.ui.screens.GameScreen
import com.example.escaperoomapp.ui.screens.HomeScreen
import com.example.escaperoomapp.ui.screens.LeaderboardScreen
import com.example.escaperoomapp.ui.screens.ResultScreen
import com.example.escaperoomapp.ui.screens.RoomDetailScreen

object Routes {
    const val HOME = "home"
    const val ROOM_DETAIL = "room_detail/{roomId}/{roomName}/{timeLimit}/{youtubeUrl}"
    const val GAME = "game/{roomId}/{timeLimit}/{roomName}"
    const val RESULT = "result/{score}/{timeUsed}/{hintsUsed}/{roomName}"
    const val LEADERBOARD = "leaderboard"
}

@Composable
fun NavGraph(
    navController: NavHostController,
    onShakeCallback: (() -> Unit) -> Unit
) {
    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            onShakeCallback {}
            HomeScreen(onRoomClick = { room, roomId ->
                val encodedUrl = android.net.Uri.encode(room.youtubeUrl)
                navController.navigate("room_detail/${roomId}/${room.name}/${room.timeLimit}/${encodedUrl}")
            })
        }

        composable(Routes.ROOM_DETAIL) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            val timeLimit = backStackEntry.arguments?.getString("timeLimit")?.toInt() ?: 30
            val youtubeUrl = backStackEntry.arguments?.getString("youtubeUrl") ?: ""
            onShakeCallback {}
            RoomDetailScreen(
                roomId = roomId, roomName = roomName, timeLimit = timeLimit,
                youtubeUrl = youtubeUrl,
                onStartGame = { navController.navigate("game/${roomId}/${timeLimit}/${roomName}") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.GAME) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            val timeLimit = backStackEntry.arguments?.getString("timeLimit")?.toInt() ?: 30
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            GameScreen(
                roomId = roomId, timeLimit = timeLimit,
                onShakeCallback = onShakeCallback,
                onGameFinished = { score, timeUsed, hintsUsed ->
                    navController.navigate("result/${score}/${timeUsed}/${hintsUsed}/${roomName}") {
                        popUpTo(Routes.HOME)
                    }
                }
            )
        }

        composable(Routes.RESULT) { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")?.toInt() ?: 0
            val timeUsed = backStackEntry.arguments?.getString("timeUsed")?.toInt() ?: 0
            val hintsUsed = backStackEntry.arguments?.getString("hintsUsed")?.toInt() ?: 0
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            onShakeCallback {}
            ResultScreen(
                score = score, timeUsed = timeUsed, hintsUsed = hintsUsed, roomName = roomName,
                onPlayAgain = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onLeaderboard = { navController.navigate(Routes.LEADERBOARD) }
            )
        }

        composable(Routes.LEADERBOARD) {
            onShakeCallback {}
            LeaderboardScreen(onBack = { navController.popBackStack() })
        }
    }
}
