package com.example.visualtaskorganizer.data.repository

import com.example.visualtaskorganizer.model.Column
import kotlinx.coroutines.flow.Flow

interface ColumnRepository {
    fun getColumnsForBoard(boardId: Int): Flow<List<Column>>

    suspend fun insertColumn(column: Column)

    suspend fun deleteColumn(column: Column)
}