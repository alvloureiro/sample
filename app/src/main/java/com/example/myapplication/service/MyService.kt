package com.example.myapplication.service

import android.app.Service
import android.content.Intent
import android.os.DeadObjectException
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log

class MyService : Service() {
    private var messenger: Messenger? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyService", "onStartCommand")
        messenger = intent?.getParcelableExtra("MESSENGER")
        sendMessageToStarter("Hello from Service")
        return START_STICKY
    }

    private fun sendMessageToStarter(message: String) {
        Log.d("MyService", "sendMessageToStarter")
        messenger?.let {
            val msg = Message.obtain(null, 1, message)
            try {
                it.send(msg)
            } catch (e: DeadObjectException) {
                Log.e("MyService", "Error sending message", e)
            }
        }
    }

    override fun onDestroy() {
        Log.d("MyService", "onDestroy")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}