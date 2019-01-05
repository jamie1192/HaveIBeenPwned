package com.jamie1192.haveibeenpwned.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper

/**
 * Created by jamie1192 on 1/1/19.
 */
internal class NotificationHelper (context: Context) : ContextWrapper(context) {

    private val notificationManager : NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

//    init {
//         val channel = NotificationC(
//
//         )
//    }

}