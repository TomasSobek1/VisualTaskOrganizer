package com.example.visualtaskorganizer.data.local

import androidx.room.*
import com.example.visualtaskorganizer.model.Board
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardDao {
    @Query("SELECT * FROM boards")
    fun getAllBoards(): Flow<List<Board>>

    @Insert
    suspend fun insertBoard(board: Board)

    @Delete
    suspend fun deleteBoard(board: Board)
}