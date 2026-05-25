package com.example.visualtaskorganizer.ui.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visualtaskorganizer.data.repository.BoardRepository
import com.example.visualtaskorganizer.data.repository.ColumnRepository
import com.example.visualtaskorganizer.data.repository.TaskRepository
import com.example.visualtaskorganizer.model.Column as EntityColumn
import com.example.visualtaskorganizer.model.Board
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

    init {
        viewModelScope.launch {
            val boards = boardRepository.getAllBoardsStream().first()
            if (boards.isEmpty()) {
                val newBoardId = boardRepository.insertBoard(Board(title = "Môj prvý projekt", colorTheme = 0))
                columnRepository.insertColumn(EntityColumn(title = "To Do", boardId = 1, orderIndex = 0))
                columnRepository.insertColumn(EntityColumn(title = "In Progress", boardId = 1, orderIndex = 1))
                columnRepository.insertColumn(EntityColumn(title = "Done", boardId = 1, orderIndex = 2))
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
        return columnRepository.getColumnsForBoard(boardId);
    }

    fun seedData(boardId: Int) {
        viewModelScope.launch {
            columnRepository.insertColumn(EntityColumn(title = "To Do", boardId = boardId, orderIndex = 0))
            columnRepository.insertColumn(EntityColumn(title = "In Progress", boardId = boardId, orderIndex = 1))
            columnRepository.insertColumn(EntityColumn(title = "Done", boardId = boardId, orderIndex = 2))
        }
    }
}