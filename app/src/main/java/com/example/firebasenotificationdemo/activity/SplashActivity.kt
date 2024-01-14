package com.example.firebasenotificationdemo.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasenotificationdemo.databinding.ActivitySplashBinding
import com.example.firebasenotificationdemo.utils.LOG_TAG
import com.example.firebasenotificationdemo.utils.launchActivity
import com.google.firebase.messaging.FirebaseMessaging

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fcmToken = task.result
                // Use the FCM token as needed
                Log.w(LOG_TAG, "SplashActivity: FCM Token:$fcmToken")

                Handler(Looper.getMainLooper()).postDelayed({
                    launchActivity(HomeActivity::class.java)
                }, 3000)
            } else {
                // Handle the error
                Log.w(LOG_TAG, "SplashActivity: FCM Token Failed")
                Handler(Looper.getMainLooper()).postDelayed({
                    launchActivity(HomeActivity::class.java)
                }, 1500)
            }
        }

    }
}