package com.example.visualtaskorganizer.data.repository

import com.example.visualtaskorganizer.data.local.ColumnDao
import com.example.visualtaskorganizer.model.Column
import kotlinx.coroutines.flow.Flow

class OfflineColumnRepository(private val columnDao: ColumnDao) : ColumnRepository {

    override fun getColumnsForBoard(boardId: Int): Flow<List<Column>> {
        return columnDao.getColumnsForBoard(boardId)
    }

    override suspend fun insertColumn(column: Column) {
        columnDao.insertColumn(column)
    }

    override suspend fun deleteColumn(column: Column) {
        columnDao.deleteColumn(column)
    }
}