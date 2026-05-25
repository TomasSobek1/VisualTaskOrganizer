package com.example.visualtaskorganizer.data.local

import androidx.room.*
import com.example.visualtaskorganizer.model.Column
import kotlinx.coroutines.flow.Flow

@Dao
interface ColumnDao {
    @Query("SELECT * FROM columns WHERE boardId = :boardId ORDER BY orderIndex ASC")
    fun getColumnsForBoard(boardId: Int): Flow<List<Column>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColumn(column: Column)

    @Delete
    suspend fun deleteColumn(column: Column)
}