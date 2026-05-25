package com.example.visualtaskorganizer.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Column::class,
            parentColumns = ["columnId"],
            childColumns = ["columnId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    val columnId: Int,
    val title: String,
    val description: String,
    val startDate: Long?,
    val deadline: Long?,
    val priority: Int,
    val colorTag: Int,
    val label: String,
    val is_completed: Boolean
)