package com.example.myapplication.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Messenger
import android.util.Log
import android.widget.RemoteViews
import com.example.myapplication.R
import com.example.myapplication.service.MyService

class MyAppWidgetProvider : AppWidgetProvider() {
    private val handler by lazy {
        Handler(
            Looper.getMainLooper()
        ) { msg ->
            Log.i("MyAppWidgetProvider", "handleMessage: ${msg.what}")
            when (msg.what) {
                1 -> {
                    Log.d(
                        "MyAppWidgetProvider",
                        "handleMessage from AppWidget Provider: ${
                            msg.obj
                        }"
                    )
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private val messenger by lazy {
        Messenger(handler)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("MyAppWidgetProvider", "onUpdate")
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        Log.d("MyAppWidgetProvider", "onAppWidgetOptionsChanged")
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onEnabled(context: Context) {
        Log.d("MyAppWidgetProvider", "onEnabled")
        // Start the service and pass the Messenger
        val serviceIntent = Intent(context, MyService::class.java).apply {
            putExtra("MESSENGER", messenger)
        }
        context.startService(serviceIntent)
    }

    override fun onDisabled(context: Context) {
        Log.d("MyAppWidgetProvider", "onDisabled")
        val intent = Intent(context, MyService::class.java)
        context.stopService(intent)
    }
}
