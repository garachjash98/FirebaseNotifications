package com.example.firebasenotificationdemo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.firebasenotificationdemo.firebasenotifications.NotificationPreview

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")

        // Handle the received intent and show the notification
        NotificationPreview.showNotification(context, title, body)
    }
}