package it.unipd.dei.music_application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    private val appClient = AppClient()
}