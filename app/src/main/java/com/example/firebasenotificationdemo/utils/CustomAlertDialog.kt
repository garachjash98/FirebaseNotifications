package com.example.firebasenotificationdemo.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class CustomAlertDialog(private val context: Context) {

    fun createDialog(
        title: String? = null,
        message: String,
        positiveButtonText: String,
        positiveButtonClickListener: DialogInterface.OnClickListener? = null
    ) {
        val builder = AlertDialog.Builder(context)
        title?.let { builder.setTitle(it) }
        builder.setMessage(message)

        positiveButtonText.let {
            builder.setPositiveButton(it, positiveButtonClickListener)
            builder.create().dismiss()
        }

        builder.create().show()
    }
}