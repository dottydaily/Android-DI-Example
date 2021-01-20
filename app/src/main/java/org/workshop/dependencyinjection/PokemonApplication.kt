package org.workshop.dependencyinjection

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokemonApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        NotificationUtils.createNotificationChannel(this)
    }
}