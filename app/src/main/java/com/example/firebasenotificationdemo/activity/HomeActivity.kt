package com.example.firebasenotificationdemo.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.firebasenotificationdemo.R
import com.example.firebasenotificationdemo.databinding.ActivityHomeBinding
import com.example.firebasenotificationdemo.preference.SharedPrefs
import com.example.firebasenotificationdemo.utils.CustomAlertDialog
import com.example.firebasenotificationdemo.utils.firebase_topic_subs
import com.example.firebasenotificationdemo.utils.fromCustomNotification
import com.example.firebasenotificationdemo.utils.gone
import com.example.firebasenotificationdemo.utils.intentNotificationId
import com.example.firebasenotificationdemo.utils.toastMsg
import com.example.firebasenotificationdemo.utils.visible
import com.google.firebase.messaging.FirebaseMessaging


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var sharedPrefs: SharedPrefs? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    this@HomeActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                init()
                getIntents(intent)
                allClicks()
            }
        } else {
            init()
            getIntents(intent)
            allClicks()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        getIntents(intent)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, proceed with notifications
                toastMsg(resources.getString(R.string.noti_permission_allow))
                init()
                allClicks()
            } else {
                toastMsg(resources.getString(R.string.noti_permission_denied))
            }
        }


    private fun getIntents(intent: Intent) {
        val fromCustomNotification = intent.getStringExtra(fromCustomNotification)
        val fromNotificationId = intent.getIntExtra(intentNotificationId, 0)
        NotificationManagerCompat.from(this@HomeActivity).cancel(fromNotificationId)
        when (fromCustomNotification){
            "accept"->{
                CustomAlertDialog(this@HomeActivity)
                    .createDialog(
                        title = resources.getString(R.string.msg_app_name),
                        message = resources.getString(R.string.set_as_default_message),
                        positiveButtonText = resources.getString(R.string.okay_txt)
                    )
            }
            "decline"->{
                CustomAlertDialog(this@HomeActivity)
                    .createDialog(
                        title = resources.getString(R.string.msg_app_name),
                        message = resources.getString(R.string.set_as_default_decline),
                        positiveButtonText = resources.getString(R.string.okay_txt)
                    )
            }
        }
    }

    private fun init() {
        sharedPrefs = SharedPrefs(this@HomeActivity)
        binding.apply {
            if (sharedPrefs?.getBoolean(SharedPrefs.SUBS_TOPIC, false) == true) {
                btnSubscribeNoti.gone()
                btnUnSubscribeNoti.visible()
                tvHomeTitle.text = resources.getText(R.string.subscribe_user_already)
            } else {
                btnSubscribeNoti.visible()
                btnUnSubscribeNoti.gone()
            }
        }
    }

    private fun allClicks() {
        binding.apply {
            btnSubscribeNoti.setOnClickListener {
                FirebaseMessaging.getInstance().subscribeToTopic(firebase_topic_subs)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Successfully Subscribed to Notification Service
                            toastMsg(resources.getString(R.string.topic_subs_done))
                            tvHomeTitle.text = resources.getText(R.string.subscribe_user_title)
                            btnSubscribeNoti.gone()
                            btnUnSubscribeNoti.visible()
                            sharedPrefs?.putBoolean(SharedPrefs.SUBS_TOPIC, true)
                        } else {
                            //Subscription  Notification Service failed
                            toastMsg(resources.getString(R.string.topic_subs_fail))
                        }
                    }
            }

            btnUnSubscribeNoti.setOnClickListener {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(firebase_topic_subs)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            toastMsg(resources.getString(R.string.topic_un_subs_done))
                            btnSubscribeNoti.visible()
                            btnUnSubscribeNoti.gone()
                            tvHomeTitle.text = resources.getText(R.string.subs_to_topic)
                            sharedPrefs?.putBoolean(SharedPrefs.SUBS_TOPIC, false)
                        } else {
                            toastMsg(resources.getString(R.string.topic_un_subs_fail))
                        }
                    }
            }
        }
    }
}