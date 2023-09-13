package com.couponusers.couponuser.Utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.test.core.app.ApplicationProvider
import com.couponusers.couponuser.Activity.DashBoard_Activity
import com.example.couponuser.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FireBaseMsgService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

//        if (remoteMessage.notification != null) {
//            remoteMessage.notification!!.body?.let {
//                remoteMessage.notification!!.title?.let { it1 ->
//                    pushNotification(it1, it)
//
//                }
//            }
//        }
//
//    }
//        fun pushNotification(title: String, msg: String) {
//
//            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.app_logo, null)
//            val bitmapDrawable = drawable as? BitmapDrawable
//            val largeIcon = bitmapDrawable?.bitmap
//
//            val nm = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//            val CHANNEL_ID = "push_noti"
//
//            val pi = PendingIntent.getActivity(this, 100,
//                Intent(this, DashBoard_Activity::class.java), PendingIntent.FLAG_IMMUTABLE
//            )
//
//            val notification: Notification
//
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val name = "Custom Channel"
//                val description = "Channel for Push Notification"
//                val importance = NotificationManager.IMPORTANCE_DEFAULT
//
//                val channel = NotificationChannel(CHANNEL_ID, name, importance)
//                channel.description = description
//
//                nm.createNotificationChannel(channel)
//
//                notification = Notification.Builder(this, CHANNEL_ID)
//                    .setLargeIcon(largeIcon)
//                    .setSmallIcon(R.drawable.app_logo)
//                    .setContentIntent(pi)
//                    .setContentTitle(title)
//                    .setSubText(msg)
//                    //.setStyle(bigPictureStyle)
//                    .setPriority(Notification.PRIORITY_HIGH)
//                    .setAutoCancel(true)
//                    .build()
//            } else {
//                notification = Notification.Builder(this)
//                    .setLargeIcon(largeIcon)
//                    .setSmallIcon(R.drawable.app_logo)
//                    .setContentIntent(pi)
//                    .setContentTitle(title)
//                    .setSubText(msg)
//                    //.setStyle(bigPictureStyle)
//                    .setPriority(Notification.PRIORITY_HIGH)
//                    .setAutoCancel(true)
//                    .build()
//            }
//
//            nm.notify(1, notification)

        if (remoteMessage.getNotification() != null) {

            remoteMessage.getNotification()!!.getTitle()?.let {
                remoteMessage.getNotification()!!.getBody()?.let { it1 ->
                    showNotification(it, it1)
                }
            }
        }


        }


    private fun getCustomDesign(title: String, message: String): RemoteViews {

        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.custom_notifcation_layout)
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.text, message)
        remoteViews.setImageViewResource(R.id.icon_noti, R.drawable.app_logo
        )
        return remoteViews
    }

    fun showNotification(
        title: String, message: String) {

        val intent = Intent(this, DashBoard_Activity::class.java)

        val channel_id = "notification_channel"

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

       val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            getApplicationContext(), channel_id
        )
            .setSmallIcon(R.drawable.noti_small_logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)


        builder = builder.setContent(getCustomDesign(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.O
        ) {
            val notificationChannel = NotificationChannel(
                channel_id, "android App",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager!!.createNotificationChannel(
                notificationChannel
            )
        }
        notificationManager!!.notify(0, builder.build())
    }

    }

