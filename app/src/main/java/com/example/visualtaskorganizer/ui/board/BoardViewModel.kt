package com.example.visualtaskorganizer.ui.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visualtaskorganizer.data.repository.BoardRepository
import com.example.visualtaskorganizer.data.repository.ColumnRepository
import com.example.visualtaskorganizer.data.repository.TaskRepository
import com.example.visualtaskorganizer.model.Column as EntityColumn
import com.example.visualtaskorganizer.model.Board
import com.example.visualtaskorganizer.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BoardViewModel(
    private val boardRepository: BoardRepository,
    private val columnRepository: ColumnRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    //test
    init {
        viewModelScope.launch {
            val boards = boardRepository.getAllBoardsStream().first()
            if (boards.isEmpty()) {
                boardRepository.insertBoard(Board(title = "Mobile App Redesign", colorTheme = 0))

                val boardId = 1
                columnRepository.insertColumn(EntityColumn(title = "To Do", boardId = boardId, orderIndex = 0))
                columnRepository.insertColumn(EntityColumn(title = "In Progress", boardId = boardId, orderIndex = 1))
                columnRepository.insertColumn(EntityColumn(title = "Done", boardId = boardId, orderIndex = 1))
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }
    }

    fun getColumnsForBoard(boardId: Int): Flow<List<EntityColumn>> {
        return columnRepository.getColumnsForBoard(boardId)
    }

    fun getTasksForColumn(columnId: Int): Flow<List<Task>> {
        return taskRepository.getTasksForColumn(columnId)
    }

    fun updateTaskColumn(taskId: Int, newColumnId: Int) {
        viewModelScope.launch {
            taskRepository.getTasksForColumn(newColumnId).first().find { it.taskId == taskId }?.let { task ->
                taskRepository.updateTask(task.copy(columnId = newColumnId))
            } ?: run {
                val columns = columnRepository.getColumnsForBoard(1).first()
                for (col in columns) {
                    val foundTask = taskRepository.getTasksForColumn(col.columnId).first().find { it.taskId == taskId }
                    if (foundTask != null) {
                        taskRepository.updateTask(foundTask.copy(columnId = newColumnId))
                        break
                    }
                }
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun addColumn(name: String) {
        viewModelScope.launch {
            //boardId sa bude menit, upravit nech orderIndex sa zvysuje po jednom
            val newColumn = EntityColumn(title = name, boardId = 1, orderIndex = 0)
            columnRepository.insertColumn(newColumn)
        }
    }

    fun deleteColumn(column: EntityColumn) {
        viewModelScope.launch {
            columnRepository.deleteColumn(column)
        }
    }
}