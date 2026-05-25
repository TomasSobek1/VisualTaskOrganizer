package com.example.visualtaskorganizer.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.visualtaskorganizer.VisualTaskOrganizerApplication
import com.example.visualtaskorganizer.ui.board.BoardViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val application = (this[androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VisualTaskOrganizerApplication)
            BoardViewModel(application.container.boardRepository,
                           application.container.columnRepository,
                           application.container.taskRepository)
        }
    }
}