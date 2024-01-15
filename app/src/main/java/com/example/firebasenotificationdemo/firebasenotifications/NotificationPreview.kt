package com.example.firebasenotificationdemo.firebasenotifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.firebasenotificationdemo.R
import com.example.firebasenotificationdemo.activity.HomeActivity
import com.example.firebasenotificationdemo.firebasenotifications.MyFirebaseMessagingService.Companion.notificationManager
import com.example.firebasenotificationdemo.utils.fromCustomNotification
import com.example.firebasenotificationdemo.utils.intentNotificationId

object NotificationPreview {

    fun showNotification(context: Context, title: String?, body: String?) {
        // Implement the code to display a custom notification UI
        // You can use NotificationCompat.Builder to build and display the notification
        val notificationLayout =
            RemoteViews(context.packageName, R.layout.custom_notification_layout)
        val notificationId = 1
        notificationLayout.setTextViewText(R.id.tvNotificationTitle, title)
        notificationLayout.setTextViewText(R.id.tvNotificationDesc, body)

        val notiAcceptBtn = Intent(context, HomeActivity::class.java)
        notiAcceptBtn.putExtra(intentNotificationId, notificationId)
        notiAcceptBtn.putExtra(fromCustomNotification, "accept")
        val notiPendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                notiAcceptBtn,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        notificationLayout.setOnClickPendingIntent(R.id.tvNotiAccept, notiPendingIntent)

        val notiDeclineBtn = Intent(context, HomeActivity::class.java)
        notiDeclineBtn.putExtra(intentNotificationId, notificationId)
        notiDeclineBtn.putExtra(fromCustomNotification, "decline")
        val notiDecPendingIntent =
            PendingIntent.getActivity(
                context,
                1,
                notiDeclineBtn,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        notificationLayout.setOnClickPendingIntent(R.id.tvNotiDecline, notiDecPendingIntent)

        val notification =
            NotificationCompat.Builder(context, MyFirebaseMessagingService.CHANNEL_ID)
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

}