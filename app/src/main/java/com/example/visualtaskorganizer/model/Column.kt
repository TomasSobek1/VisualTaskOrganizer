package com.example.visualtaskorganizer.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "columns",
    foreignKeys = [
        ForeignKey(
            entity = Board::class,
            parentColumns = ["boardId"],
            childColumns = ["boardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Column(
    @PrimaryKey(autoGenerate = true) val columnId: Int = 0,
    val boardId: Int,
    val title: String,
    val orderIndex: Int
)