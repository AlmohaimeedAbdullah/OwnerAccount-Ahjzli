package com.tuwaiq.owneraccount.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tuwaiq.owneraccount.MainActivity
import com.tuwaiq.owneraccount.R

class NotificationHelper(val context: Context) {
    private val CHANNEL_ID = "movie_channel_id"
    private val NOTIFICATION_ID = 1

    @RequiresApi(Build.VERSION_CODES.S)
    fun createNotification(title: String, message: String){
        createNotificationChannel()
        val intent= Intent(context, MainActivity::class.java).apply {
            flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.putExtra("titleTest","messageTest")
        }

        val pendingIntent = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_MUTABLE)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notificatios)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID,notification)

    }

    private fun createNotificationChannel(){

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_ID, importance).apply {
                description = "Anime Channel description"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }
