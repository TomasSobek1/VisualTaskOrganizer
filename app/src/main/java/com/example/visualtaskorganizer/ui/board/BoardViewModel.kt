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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
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

                val columnId1 = 1
                val columnId2 = 2

                taskRepository.insertTask(Task(
                    taskId = 0,
                    columnId = columnId1,
                    title = "Create wireframes",
                    description = "Design phase",
                    startDate = 1714000000L,
                    deadline = 1715000000L,
                    priority = 1,
                    colorTag = 0xFF0000,
                    label = "Design",
                    is_completed = false
                ))

                taskRepository.insertTask(Task(
                    taskId = 0,
                    columnId = columnId2,
                    title = "Auth System",
                    description = "Backend logic",
                    startDate = 1714000000L,
                    deadline = 1716000000L,
                    priority = 1,
                    colorTag = 0x00FF00,
                    label = "Dev",
                    is_completed = false
                ))
            }
        }
    }

    val boardList: StateFlow<List<Board>> = boardRepository.getAllBoardsStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addBoard(title: String, colorTheme: Int) {
        viewModelScope.launch {
            boardRepository.insertBoard(Board(title = title, colorTheme = colorTheme))
        }
    }

    fun getColumnsForBoard(boardId: Int): Flow<List<EntityColumn>> {
        return columnRepository.getColumnsForBoard(boardId)
    }

    fun getTasksForColumn(columnId: Int): Flow<List<Task>> {
        return taskRepository.getTasksForColumn(columnId)
    }

    fun moveTaskToColumn(task: Task, newColumnId: Int) {
        viewModelScope.launch {
            val updatedTask = task.copy(columnId = newColumnId)
            taskRepository.updateTask(updatedTask)
        }
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
}