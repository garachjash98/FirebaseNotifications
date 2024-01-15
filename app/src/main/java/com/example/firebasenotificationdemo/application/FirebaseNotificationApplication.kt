package com.example.firebasenotificationdemo.application

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.inappmessaging.FirebaseInAppMessaging

class FirebaseNotificationApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseInAppMessaging.getInstance().isAutomaticDataCollectionEnabled = true
    }
}