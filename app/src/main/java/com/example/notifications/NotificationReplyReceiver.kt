package com.example.notifications

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput

class NotificationReplyReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        // Retrieve the text input from the RemoteInput
        val replyText = RemoteInput.getResultsFromIntent(intent)?.getCharSequence("KEY_TEXT_REPLY")
        if (replyText != null) {
            // Display the reply message or process it as needed
            Toast.makeText(context, "Reply received: $replyText", Toast.LENGTH_SHORT).show()

            // Update the notification to indicate the reply was received
            val notificationId = 2 // Same ID used for the original notification
            val updatedNotification = NotificationCompat.Builder(context, "COURSE_NOTIFICATIONS")
                .setSmallIcon(R.drawable.ic_launcher_background) // Same icon
                .setContentTitle("Reply sent") // Updated title
                .setContentText("Your reply : $replyText was sent.") // Updated content text
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true) // Automatically remove notification when tapped

            // Show the updated notification
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(notificationId, updatedNotification.build())
        }
    }
}
