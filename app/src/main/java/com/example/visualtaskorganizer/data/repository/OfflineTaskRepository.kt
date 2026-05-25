package com.example.visualtaskorganizer.data.repository

import com.example.visualtaskorganizer.data.local.TaskDao
import com.example.visualtaskorganizer.model.Task
import kotlinx.coroutines.flow.Flow

class OfflineTaskRepository(private val taskDao: TaskDao) : TaskRepository {

    override fun getTasksForColumn(columnId: Int): Flow<List<Task>> {
        return taskDao.getTasksForColumn(columnId)
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}