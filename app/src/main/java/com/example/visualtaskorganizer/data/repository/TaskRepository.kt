package com.example.visualtaskorganizer.data.repository

import com.example.visualtaskorganizer.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getTasksForColumn(columnId: Int): Flow<List<Task>>

    suspend fun insertTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)
}