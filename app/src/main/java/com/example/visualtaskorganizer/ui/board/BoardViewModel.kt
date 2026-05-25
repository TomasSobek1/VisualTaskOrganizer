package com.example.visualtaskorganizer.ui.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visualtaskorganizer.data.repository.BoardRepository
import com.example.visualtaskorganizer.data.repository.ColumnRepository
import com.example.visualtaskorganizer.data.repository.TaskRepository
import com.example.visualtaskorganizer.model.Board
import com.example.visualtaskorganizer.model.Column as EntityColumn
import com.example.visualtaskorganizer.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BoardViewModel(
    private val boardRepository: BoardRepository,
    private val columnRepository: ColumnRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

//ukazkove data vygenerovane cez AI
init {
    viewModelScope.launch {
        val boards = boardRepository.getAllBoardsStream().first()
        if (boards.isEmpty()) {
            // Board
            boardRepository.insertBoard(Board(1, "Mobile App Development", 0xFF6750A4.toInt()))

            // Columns
            columnRepository.insertColumn(EntityColumn(1, 1, "To Do", 0))
            columnRepository.insertColumn(EntityColumn(2, 1, "In Progress", 1))
            columnRepository.insertColumn(EntityColumn(3, 1, "Done", 2))

            // Pomocné premenné pre dátumy
            val now = System.currentTimeMillis()
            val day = 24 * 60 * 60 * 1000L

            // Tasky
            // 1. To Do
            taskRepository.insertTask(Task(
                0, 1, "Design UI", "Create wireframes", now, now + 3 * day, 3, 0xFFE57373.toInt(), "Design", false
            ))

            // 2. In Progress (Task A)
            taskRepository.insertTask(Task(
                0, 2, "Setup DB", "Configure Room", now - day, now + day, 2, 0xFF64B5F6.toInt(), "Backend", false
            ))

            // 3. In Progress (Task B) - druhý task v stĺpci
            taskRepository.insertTask(Task(
                0, 2, "Fix Navigation", "Check back stack logic", now, now + 2 * day, 2, 0xFFBA68C8.toInt(), "UI", false
            ))

            // 4. Done
            taskRepository.insertTask(Task(
                0, 3, "Docs", "Finalize report", now - 7 * day, now - day, 1, 0xFF81C784.toInt(), "Report", false
            ))
        }
    }
}

    // --- Board operácie ---

    fun getBoard(boardId: Int): Flow<Board?> {
        return boardRepository.getBoardById(boardId)
    }

    val allBoards: Flow<List<Board>> = boardRepository.getAllBoardsStream()

    fun addBoard(title: String, color: Int) {
        viewModelScope.launch {
            boardRepository.insertBoard(Board(title = title, colorTheme = color))
        }
    }

    fun deleteBoard(board: Board) {
        viewModelScope.launch {
            boardRepository.deleteBoard(board)
        }
    }

    // --- Column operácie ---

    fun getColumnsForBoard(boardId: Int): Flow<List<EntityColumn>> {
        return columnRepository.getColumnsForBoard(boardId)
    }

    fun addColumn(boardId: Int, name: String) {
        viewModelScope.launch {
            val newColumn = EntityColumn(title = name, boardId = boardId, orderIndex = 0)
            columnRepository.insertColumn(newColumn)
        }
    }

    fun deleteColumn(column: EntityColumn) {
        viewModelScope.launch {
            columnRepository.deleteColumn(column)
        }
    }

    // --- Task operácie ---

    fun getTasksForColumn(columnId: Int): Flow<List<Task>> {
        return taskRepository.getTasksForColumn(columnId)
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    // presun ulohy medzi stlpcami
    fun updateTaskColumn(taskId: Int, newColumnId: Int, currentBoardId: Int) {
        viewModelScope.launch {
            val taskInColumn = taskRepository.getTasksForColumn(newColumnId).first().find { it.taskId == taskId }

            if (taskInColumn != null) {
                taskRepository.updateTask(taskInColumn.copy(columnId = newColumnId))
            } else {
                val columns = columnRepository.getColumnsForBoard(currentBoardId).first()
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