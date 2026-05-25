package com.example.visualtaskorganizer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.visualtaskorganizer.ui.board.AddTaskScreen
import com.example.visualtaskorganizer.ui.board.BoardViewScreen
import com.example.visualtaskorganizer.ui.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home_screen") {

        // Home screen
        composable("home_screen") {
            HomeScreen(onBoardClick = { boardId ->
                navController.navigate("board_view/$boardId")
            })
        }

        // Board view
        composable(
            route = "board_view/{boardId}",
            arguments = listOf(navArgument("boardId") { type = NavType.IntType })
        ) { backStackEntry ->
            val boardId = backStackEntry.arguments?.getInt("boardId") ?: 1
            BoardViewScreen(
                boardId = boardId,
                onAddTaskClick = { columnId -> navController.navigate("add_task/$columnId") },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Add task
        composable(
            route = "add_task/{columnId}",
            arguments = listOf(navArgument("columnId") { type = NavType.IntType })
        ) { backStackEntry ->
            val columnId = backStackEntry.arguments?.getInt("columnId") ?: 1
            AddTaskScreen(
                columnId = columnId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}