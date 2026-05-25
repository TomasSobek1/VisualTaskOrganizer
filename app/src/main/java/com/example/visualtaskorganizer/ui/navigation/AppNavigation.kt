package com.example.visualtaskorganizer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.visualtaskorganizer.ui.board.BoardViewScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "board_view/1") {
        composable("board_view/{boardId}") { backStackEntry ->
            val boardId = backStackEntry.arguments?.getString("boardId")?.toInt() ?: 1
            BoardViewScreen(
                boardId = boardId,
                onAddTaskClick = { /* Akcia po kliknutí na FAB */ }
            )
        }
    }
}