package com.example.firebasenotificationdemo.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast


const val LOG_TAG = "FIREBASE_NOTIFICATIONS"


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

//Intent Call
fun <T> Activity.launchActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

//Toast
fun Activity.toastMsg(toastMessage: String) {
    Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
}

//Common Keys
const val firebase_topic_subs = "home_set_as_default_cancel"
const val fromCustomNotification = "fromCustomNotification"
const val intentNotificationId = "notificationId"