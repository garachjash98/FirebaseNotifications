package com.example.firebasenotificationdemo.firebasenotifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.firebasenotificationdemo.R
import com.example.firebasenotificationdemo.activity.HomeActivity
import com.example.firebasenotificationdemo.utils.LOG_TAG
import com.example.firebasenotificationdemo.utils.fromCustomNotification
import com.example.firebasenotificationdemo.utils.intentNotificationId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var notificationManager: NotificationManager? = null

    companion object {
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
            remoteMessage.data.let {
                // Extract data and build custom notification
                Log.w(
                    LOG_TAG,
                    "MyFirebaseMessagingService: onMessageReceived" + remoteMessage.data["title"]
                )
                showNotification(remoteMessage.data["title"], remoteMessage.data["body"])
            }
        }

    }

    private fun showNotification(title: String?, body: String?) {
        // Implement the code to display a custom notification UI
        // You can use NotificationCompat.Builder to build and display the notification
        val notificationLayout = RemoteViews(packageName, R.layout.custom_notification_layout)
        val notificationId = 1
        notificationLayout.setTextViewText(R.id.tvNotificationTitle, title)
        notificationLayout.setTextViewText(R.id.tvNotificationDesc, body)

        val notiAcceptBtn = Intent(this, HomeActivity::class.java)
        notiAcceptBtn.putExtra(intentNotificationId, notificationId)
        notiAcceptBtn.putExtra(fromCustomNotification, "accept")
        val notiPendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                notiAcceptBtn,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        notificationLayout.setOnClickPendingIntent(R.id.tvNotiAccept, notiPendingIntent)

        val notiDeclineBtn = Intent(this, HomeActivity::class.java)
        notiDeclineBtn.putExtra(intentNotificationId, notificationId)
        notiDeclineBtn.putExtra(fromCustomNotification, "decline")
        val notiDecPendingIntent =
            PendingIntent.getActivity(
                this,
                1,
                notiDeclineBtn,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        notificationLayout.setOnClickPendingIntent(R.id.tvNotiDecline, notiDecPendingIntent)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_message_icon)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomBigContentView(notificationLayout)
            .setAutoCancel(true)
            .build()

        notificationManager?.notify(notificationId, notification)
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