package com.example.visualtaskorganizer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.visualtaskorganizer.model.Board
import com.example.visualtaskorganizer.model.Column
import com.example.visualtaskorganizer.model.Task

@Database(entities = [Board::class, Column::class, Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun boardDao(): BoardDao
    abstract fun columnDao(): ColumnDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "task_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}