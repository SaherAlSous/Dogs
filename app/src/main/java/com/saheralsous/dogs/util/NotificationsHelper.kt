package com.saheralsous.dogs.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.saheralsous.dogs.R
import com.saheralsous.dogs.view.MainActivity

class NotificationsHelper(val context: Context) { //video 54

    private val CHANNEL_ID = "Dogs Channel Id"
    private val NOTIFICATION_ID = 123

    fun createNotification(){
        createNotificationChannel()
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            /**When user clicks on the notification it will open
             *  a new activity if there is no activity exist
             *  if there is an activity already, it clear it and go for the clicked on
             */
        }
        val pendingIntent = PendingIntent.getActivity(context, 0,intent, 0)

        val icon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_dog)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.dog_icon) //top bar icon (screen top) app icon
            .setLargeIcon(icon) // large icon within the notification area
            .setContentTitle("Dogs retrieved")
            .setContentText("Whatever text you want")
            .setStyle(
                NotificationCompat.BigPictureStyle() //when we expand the icon we see the large picture
                    .bigPicture(icon)
                    .bigLargeIcon(null) //the icon will disappear, and only big picture will show
            )
            .setContentIntent(pendingIntent) //when the user click on an icon, this works and run the intent as programmed
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification) // run the notification.

    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = CHANNEL_ID
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name,importance).apply {
                description = descriptionText
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}