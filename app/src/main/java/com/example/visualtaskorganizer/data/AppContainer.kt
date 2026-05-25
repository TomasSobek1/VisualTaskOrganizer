package com.example.visualtaskorganizer.data

import android.content.Context
import com.example.visualtaskorganizer.data.local.AppDatabase
import com.example.visualtaskorganizer.data.repository.*

interface AppContainer {
    val boardRepository: BoardRepository
    val columnRepository: ColumnRepository
    val taskRepository: TaskRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    override val boardRepository: BoardRepository by lazy {
        OfflineBoardRepository(database.boardDao())
    }

    override val columnRepository: ColumnRepository by lazy {
        OfflineColumnRepository(database.columnDao())
    }

    override val taskRepository: TaskRepository by lazy {
        OfflineTaskRepository(database.taskDao())
    }
}