package com.example.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.notifications.ui.theme.NotificationsTheme

class MainActivity : ComponentActivity() {


    private val notificationPermissionCode = 1


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        val sendButton = findViewById<Button>(R.id.sendNotificationButton)
        sendButton.setOnClickListener {
            val title = findViewById<EditText>(R.id.notificationTitle).text.toString()
            val message = findViewById<EditText>(R.id.notificationMessage).text.toString()
            sendNotification(title, message)
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
    private fun sendNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(this, "COURSE_NOTIFICATIONS")
            .setSmallIcon(R.drawable.ic_launcher_background) // icon
            .setContentTitle(title) // Title
            .setContentText(message) // Content
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Priority
            .setColor(Color.Blue.toArgb()) // Color
            .setVibrate(longArrayOf(0, 500, 1000)) // Set vibration time
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line...")) // For bigger notification size

        val notificationManager = NotificationManagerCompat.from(this)
        // Check Permission or request it
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                notificationPermissionCode)
        }else{
            // Send notification
            notificationManager.notify(1, builder.build())
        }

    }
}


