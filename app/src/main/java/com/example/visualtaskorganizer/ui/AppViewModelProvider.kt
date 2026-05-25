package com.example.visualtaskorganizer.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.visualtaskorganizer.VisualTaskOrganizerApplication
import com.example.visualtaskorganizer.ui.board.BoardViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            // Získanie aplikácie z kontextu
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VisualTaskOrganizerApplication)

            // Injektovanie repozitárov do ViewModelu
            BoardViewModel(
                application.container.boardRepository,
                application.container.columnRepository,
                application.container.taskRepository
            )
        }
    }
}