package com.example.visualtaskorganizer.data.repository

import com.example.visualtaskorganizer.data.local.BoardDao
import com.example.visualtaskorganizer.model.Board
import kotlinx.coroutines.flow.Flow

class OfflineBoardRepository(private val boardDao: BoardDao) : BoardRepository {
    override fun getAllBoardsStream(): Flow<List<Board>> = boardDao.getAllBoards()

    override fun getBoardById(id: Int): Flow<Board?> = boardDao.getBoardById(id)

    override suspend fun insertBoard(board: Board) = boardDao.insertBoard(board)

    override suspend fun deleteBoard(board: Board) = boardDao.deleteBoard(board)
}