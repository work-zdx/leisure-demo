package com.star.content

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat


class BackgroundService : Service() {

    private val tag = "BackgroundService"

    private val channelId = 100

    private var lockScreenBroadcastReceiver: LockScreenBroadcastReceiver? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(tag, "BackgroundService StartCommand")
        return START_STICKY
    }

    override fun onCreate() {
        Log.e(tag, "BackgroundService Create")
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId: String = channelId.toString()
            val channelName = "正在使用BackgroundService"
            val channel = NotificationChannel(
                channelId, channelName,
                NotificationManager.IMPORTANCE_MIN
            )
            val manager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            startForeground(this.channelId, createNormalNotification())
        }

        lockScreenBroadcastReceiver = LockScreenBroadcastReceiver()

        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        registerReceiver(lockScreenBroadcastReceiver, filter)

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNormalNotification(): Notification? {
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId.toString())

        builder.setContentTitle(getString(R.string.app_name))
            .setContentText("BackgroundService...")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .build()

        return builder.build()
    }

    override fun onDestroy() {
        Log.e(tag, "BackgroundService Destroy")
        super.onDestroy()

        if(lockScreenBroadcastReceiver != null){
            unregisterReceiver(lockScreenBroadcastReceiver)
        }
    }
}