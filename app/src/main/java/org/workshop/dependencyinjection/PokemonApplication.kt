package org.workshop.dependencyinjection

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.workshop.dependencyinjection.notification.NotificationUtils

@HiltAndroidApp
class PokemonApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        NotificationUtils.createNotificationChannel(this)
    }
}