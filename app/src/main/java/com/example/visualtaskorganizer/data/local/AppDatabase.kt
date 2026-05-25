package com.example.visualtaskorganizer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.visualtaskorganizer.model.Board
import com.example.visualtaskorganizer.model.Column
import com.example.visualtaskorganizer.model.Task

@Database(entities = [Board::class, Column::class, Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun boardDao(): BoardDao
}