package com.example.visualtaskorganizer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boards")
data class Board(
    @PrimaryKey(autoGenerate = true) val boardId: Int = 0,
    val title: String,
    val colorTheme: Int
)