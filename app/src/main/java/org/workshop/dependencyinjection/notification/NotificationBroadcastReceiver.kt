package org.workshop.dependencyinjection.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationBroadcastReceiver: BroadcastReceiver() {
    companion object {
        const val TAG = "DependencyInjection"
        const val NOTIFICATION_ID = 101
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = "Tapped on notification!"
        Log.d(TAG, message)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        // Get NotificationId from intent to auto-remove the notification when do the action.
        context?.let {
            NotificationManagerCompat.from(it).run {
                cancel(NOTIFICATION_ID)
            }
        }
    }
}