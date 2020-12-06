package com.exomatik.manajemenpangkat.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.exomatik.manajemenpangkat.R

class BackgroundService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val builder = NotificationCompat.Builder(context, "notify Manajemen Pangkat")
        builder.setSmallIcon(R.drawable.ic_notif)
        builder.setContentTitle("Manajemen Pangkat")
        builder.setContentText("Hari ini Anda sudah dapat mengajukan usulan")
        builder.priority = NotificationCompat.PRIORITY_DEFAULT

        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText("Hari ini Anda sudah dapat mengajukan usulan")
        bigText.setBigContentTitle("Manajemen Pangkat")
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        builder.setSound(uri)
        builder.setLights(Color.GREEN, 500, 500)
        builder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(200, builder.build())

    }
}