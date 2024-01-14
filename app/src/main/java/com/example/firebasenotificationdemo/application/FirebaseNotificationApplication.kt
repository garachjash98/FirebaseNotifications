package com.example.firebasenotificationdemo.application

import android.app.Application
import com.google.firebase.FirebaseApp

class FirebaseNotificationApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}