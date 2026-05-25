package com.example.visualtaskorganizer

import android.app.Application
import com.example.visualtaskorganizer.data.AppContainer
import com.example.visualtaskorganizer.data.AppDataContainer

class VisualTaskOrganizerApplication : Application() {

    // Dependency Injection kontajner
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}