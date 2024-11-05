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


    // TODO - Create a Notification Channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // here set the notification Channel
        }
    }

    // TODO - Define and add a button with the BroadcastReceiver in NotificationActionReceiver
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun sendActionNotification(title: String, message: String, longMessage : String) {

        val actionIntent = Intent(this, NotificationActionReceiver::class.java)
        // here declare the action pending intent, using the action intent

        val builder = buildNotificationBase(title, message, longMessage)
            // Here add the Action on the builder

        sendNotification(builder, 1)

    }

    // TODO - declare and build a basic notification with text content, title, bigText
    private fun buildNotificationBase(title: String, message: String, longMessage : String): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(this, "COURSE_NOTIFICATIONS")
            .setSmallIcon(R.drawable.ic_launcher_background) // Base icon
            // here declare the rest of the notification (title, text content, bigText, color, etc ...)

        return builder
    }

    // TODO - Send notification if permissions accorded, or request permissions
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun sendNotification(builder : NotificationCompat.Builder, id : Int){
        val notificationManager = NotificationManagerCompat.from(this)

        // Check permission or request it for Android 13 and above
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions
        } else {
            // Send notification here (with .notify function)
        }
    }


    // TODO - Create a notification with an input and a send button with the BroadcastReceiver in NotificationReplyReceiver
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun sendReplyNotification(title: String, message: String, longMessage: String) {

        // TODO - Create the RemoteInput to capture user reply

        // Intent and PendingIntent for handling the reply action
        val replyIntent = Intent(this, NotificationReplyReceiver::class.java)
        val replyPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        // TODO - Create the action with RemoteInput for the reply button

        // Build the notification with the reply action
        val builder = buildNotificationBase(title, message, longMessage)
            // Add here the reply action !

        sendNotification(builder , 2)
    }

    // TODO - Update the progress bar notification with time
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun simulateProgress(title: String, message: String, longMessage: String) {
        // Coroutine for simulate progress
        CoroutineScope(Dispatchers.IO).launch {
            val maxProgress = 100
            var currentProgress = 0

            val builder = buildNotificationBase(title, message, longMessage)
                // TODO - Here declare your progress bar

            sendNotification(builder, 3)

            // Simulate progress time
            while (currentProgress < maxProgress) {
                delay(1000) // Simulate progress delay
                currentProgress += 5

                // TODO - Here update the notification progress

            }

            // TODO - Here is the end of progress

        }
    }


}


