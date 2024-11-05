package com.example.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Check if the button is correctly responding
        Toast.makeText(context, "Button notification clicked !", Toast.LENGTH_SHORT).show()
    }
}
