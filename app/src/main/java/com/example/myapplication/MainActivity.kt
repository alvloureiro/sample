package com.example.myapplication

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Messenger
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.myapplication.data.ButtonStateEntity
import com.example.myapplication.data.ExampleViewModel

@SuppressLint("LogConditional")
class MainActivity : AppCompatActivity() {
    internal val exampleViewModel: ExampleViewModel by viewModels()

    internal val handler by lazy {
        Handler(
            Looper.getMainLooper()
        ) { msg ->
            Log.i("MainActivity", "handleMessage: ${msg.what}")
            when (msg.what) {
                1 -> {
                    Log.d("MainActivity", "handleMessage: ${msg.obj}")
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
    private var colorIndex = 0
    private var isChangingColor = false
    private val colors = listOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Cyan,
        Color.Magenta,
        Color.Gray,
        Color.Black,
        Color.White,
    )
    private var buttons = mutableListOf<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttons.add(findViewById(R.id.button1))
        buttons.add(findViewById(R.id.button2))
        buttons.add(findViewById(R.id.button3))
        buttons.add(findViewById(R.id.button4))
        buttons.add(findViewById(R.id.button5))
        buttons.add(findViewById(R.id.button6))
        buttons.add(findViewById(R.id.button7))
        buttons.add(findViewById(R.id.button8))
        buttons.add(findViewById(R.id.button9))
        buttons.add(findViewById(R.id.button10))
        buttons.add(findViewById(R.id.button11))
        buttons.add(findViewById(R.id.button12))

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            Log.d("MainActivity", "startButton clicked")
            if (isChangingColor) {
                handler.removeCallbacksAndMessages(null)
                isChangingColor = false
                startButton.text = "Start"
                return@setOnClickListener
            }
            startChangeColor()
            isChangingColor = true
            startButton.text = "Stop"
        }

        setButtonsClickEvent()
    }

    private fun startChangeColor() {
        Log.d("MainActivity", "startChangeColor")
        val changeColor = object : Runnable {
            override fun run() {
                buttons.forEach {
                    Log.d("MainActivity", "changeColor: $colorIndex")
                    exampleViewModel.insert(
                        ButtonStateEntity(
                            it.id,
                            colors[colorIndex].toArgb()
                        )
                    )
                    it.setBackgroundColor(colors[colorIndex].toArgb())
                    colorIndex = (colorIndex + 1) % colors.size
                }
                handler.postDelayed(this, 200.toLong())
            }
        }

        handler.post(changeColor)
    }

    private fun setButtonsClickEvent() {
        buttons.forEach {
            it.setOnClickListener { button ->
                handler.post {
                    Log.d(
                        "MainActivity",
                        "setButtonsClickEvent ${button.id} - with color: " +
                            "${(button.background as ColorDrawable).color} clicked"
                    )
                    val container = findViewById<View>(R.id.container)
                    val currentColor = (container.background as ColorDrawable).color
                    val color = (button.background as ColorDrawable).color
                    val colorAnimation = ValueAnimator.ofArgb(currentColor, color)
                    colorAnimation.duration = 500
                    colorAnimation.addUpdateListener { animator ->
                        container.setBackgroundColor(animator.animatedValue as Int)
                    }
                    colorAnimation.start()
                }
            }
        }
    }

    override fun onPause() {
        Log.d("MainActivity", "onPause")
        handler.removeCallbacksAndMessages(null)

        super.onPause()
    }
}
