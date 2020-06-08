package com.nabeel.themoviedbtest.util

import android.app.Application
import android.content.Context
import com.nabeel.themoviedbtest.repository.Repository

val repository: Repository by lazy {
    App.repository
}

class App : Application() {
    companion object {
        lateinit var appContext: Context
        lateinit var repository: Repository
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        repository = Repository(applicationContext)
    }
}