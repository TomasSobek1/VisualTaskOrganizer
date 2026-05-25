package com.example.visualtaskorganizer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.visualtaskorganizer.ui.board.BoardViewScreen
import com.example.visualtaskorganizer.ui.board.AddTaskScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "board_view/1") {

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

        composable(
            route = "board_view/{boardId}",
            arguments = listOf(navArgument("boardId") { type = NavType.IntType })
        ) { backStackEntry ->
            val boardId = backStackEntry.arguments?.getInt("boardId") ?: 1
            BoardViewScreen(
                boardId = boardId,
                onAddTaskClick = { columnId -> navController.navigate("add_task/$columnId") }
            )
        }
    }
}