package com.example.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    private val notificationPermissionCode = 1


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        val sendButton = findViewById<Button>(R.id.sendNotificationButton)
        val sendMessageNotificationButton = findViewById<Button>(R.id.sendMessageNotificationButton)
        var sendProgressBarButton = findViewById<Button>(R.id.sendProgressBarButton)

        sendMessageNotificationButton.setOnClickListener {
            val title = findViewById<EditText>(R.id.notificationTitle).text.toString()
            val message = findViewById<EditText>(R.id.notificationMessage).text.toString()
            var notificationLongMessage = findViewById<EditText>(R.id.notificationLongMessage).text.toString()
            sendReplyNotification(title, message, notificationLongMessage)
        }
        sendButton.setOnClickListener {
            val title = findViewById<EditText>(R.id.notificationTitle).text.toString()
            val message = findViewById<EditText>(R.id.notificationMessage).text.toString()
            var notificationLongMessage = findViewById<EditText>(R.id.notificationLongMessage).text.toString()
            sendActionNotification(title, message, notificationLongMessage)
        }
        sendProgressBarButton.setOnClickListener {
            val title = findViewById<EditText>(R.id.notificationTitle).text.toString()
            val message = findViewById<EditText>(R.id.notificationMessage).text.toString()
            var notificationLongMessage = findViewById<EditText>(R.id.notificationLongMessage).text.toString()
            simulateProgress(title, message, notificationLongMessage)
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Course Notifications"
            val descriptionText = "Notifications for course reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT // Here for notification importance
            val channel = NotificationChannel("COURSE_NOTIFICATIONS", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun sendActionNotification(title: String, message: String, longMessage : String) {

        // Adding a button with an action defined in com.example.notifications.NotificationActionReceiver
        val actionIntent = Intent(this, NotificationActionReceiver::class.java)
        val actionPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            actionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = buildNotificationBase(title, message, longMessage)
            .addAction(R.drawable.ic_launcher_background, "Action", actionPendingIntent) // Action button

        sendNotification(builder, 1)

    }

    private fun buildNotificationBase(title: String, message: String, longMessage : String): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(this, "COURSE_NOTIFICATIONS")
            .setSmallIcon(R.drawable.ic_launcher_background) // icon
            .setContentTitle(title) // Title
            .setContentText(message) // Content
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Priority
            .setColor(Color.Blue.toArgb()) // Color
            .setVibrate(longArrayOf(0, 500, 1000)) // Set vibration time
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(longMessage)) // text when notification is expanded

        return builder
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun sendNotification(builder : NotificationCompat.Builder, id : Int){
        val notificationManager = NotificationManagerCompat.from(this)

        // Check permission or request it for Android 13 and above
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                notificationPermissionCode
            )
        } else {
            // Send notification with reply action
            notificationManager.notify(id , builder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun sendReplyNotification(title: String, message: String, longMessage: String) {

        // Create the RemoteInput to capture user reply
        val replyLabel = "Enter your reply here"
        val remoteInput: RemoteInput = RemoteInput.Builder("KEY_TEXT_REPLY")
            .setLabel(replyLabel)
            .build()

        // Create an Intent and PendingIntent for handling the reply action
        val replyIntent = Intent(this, NotificationReplyReceiver::class.java)
        val replyPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        // Create the action with RemoteInput for the reply button
        val replyAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
            R.drawable.ic_launcher_background, // icon for the action button
            "Reply", // label for the action
            replyPendingIntent // pending intent to handle the reply
        ).addRemoteInput(remoteInput).build()

        // Build the notification with the reply action
        val builder = buildNotificationBase(title, message, longMessage)
            .addAction(replyAction) // add the reply action

        sendNotification(builder , 2) // Send notification with permissions
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun simulateProgress(title: String, message: String, longMessage: String) {
        // Coroutine for simulate download
        CoroutineScope(Dispatchers.IO).launch {
            val maxProgress = 100
            var currentProgress = 0

            val builder = buildNotificationBase(title, message, longMessage)
                .setOnlyAlertOnce(true)
                .setProgress(maxProgress, currentProgress, false) // init progress bar

            sendNotification(builder, 3)

            // Simulate progress time
            while (currentProgress < maxProgress) {
                delay(1000) // Simulate progress delay
                currentProgress += 5

                // Here update the notification progress
                builder.setProgress(maxProgress, currentProgress, false)
                sendNotification(builder, 3)
            }

            // End of progress
            builder.setContentText("Progress complete")
                .setProgress(0, 0, false) // Delete progress bar
                .setAutoCancel(true) // Cancel notification while clicked

            sendNotification(builder, 3)
        }
    }


}


