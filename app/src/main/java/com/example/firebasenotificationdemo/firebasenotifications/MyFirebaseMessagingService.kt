package com.example.firebasenotificationdemo.firebasenotifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.firebasenotificationdemo.receiver.NotificationReceiver
import com.example.firebasenotificationdemo.utils.LOG_TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {


    companion object {
        var notificationManager: NotificationManager? = null
        const val CHANNEL_ID = "your_channel_id"
        const val CHANNEL_NAME = "Your Channel Name"
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        notificationManager?.cancelAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager?.cancelAll()
    }

    override fun onNewToken(token: String) {
        Log.w(LOG_TAG, "MyFirebaseMessagingService: New FCM Token:$token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle the received message and show a notification
        if (remoteMessage.data.isNotEmpty()) {
            // Handle data payload
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            val delay = remoteMessage.data["delay"]

            if (delay != "0") {
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, NotificationReceiver::class.java)
                    .putExtra("title", title)
                    .putExtra("body", body)

                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val delayInMillis = delay?.toInt()!! * 60 * 1000 // 5 minutes in milliseconds
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + delayInMillis,
                    pendingIntent
                )
            } else {
                NotificationPreview.showNotification(
                    this,
                    title,
                    body
                )
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                // Configure channel settings here, such as description, vibration, LED light, etc.
                description = "Channel for general notifications"
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}