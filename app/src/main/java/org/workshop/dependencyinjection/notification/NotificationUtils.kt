package org.workshop.dependencyinjection.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import org.workshop.dependencyinjection.MainActivity
import org.workshop.dependencyinjection.R

object NotificationUtils {
    const val CHANNEL_ID = "GAME_NOTIFICATION_CHANNEL_ID"
    fun createNotificationBuilder(
        context: Context,
        channelId: String,
        title: String,
        content: String,
        actionIntent: Intent? = null,
    ): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_joystick)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (actionIntent != null) {
            val actionPendingIntent: PendingIntent = PendingIntent.getBroadcast(
                    context, 0, actionIntent, 0)
            builder.addAction(R.drawable.ic_joystick, "Start new game", actionPendingIntent)
        }

        return builder
    }

    fun createExpandedNotificationBuilder(
        context: Context,
        channelId: String,
        title: String,
        content: String,
        multiLineContent: String,
        actionIntent: Intent? = null,
    ): NotificationCompat.Builder {
        val builder = createNotificationBuilder(
                context, channelId, title, content, actionIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(multiLineContent))

        return builder
    }

    fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel. Valid only on API 26+.
        // NotificationChannel is appearing on android 8.0+.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Game result"
            val descriptionText = "Tell the final status of the game"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}