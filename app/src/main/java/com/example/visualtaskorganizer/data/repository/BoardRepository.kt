package com.example.visualtaskorganizer.data.repository

import com.example.visualtaskorganizer.model.Board
import kotlinx.coroutines.flow.Flow

interface BoardRepository {
    fun getAllBoardsStream(): Flow<List<Board>>
    suspend fun insertBoard(board: Board)
    suspend fun deleteBoard(board: Board)
}