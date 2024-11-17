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
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
    private var colors = mutableListOf<Int>()
    private var buttons = mutableListOf<Button>()
    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        startTime = System.currentTimeMillis()
        super.onCreate(savedInstanceState)
        colors = mutableListOf(
            ContextCompat.getColor(this, R.color.purple_200),
            ContextCompat.getColor(this, R.color.purple_500),
            ContextCompat.getColor(this, R.color.purple_700),
            ContextCompat.getColor(this, R.color.teal_200),
            ContextCompat.getColor(this, R.color.teal_700),
            ContextCompat.getColor(this, android.R.color.holo_red_light),
            ContextCompat.getColor(this, android.R.color.holo_blue_light),
            ContextCompat.getColor(this, android.R.color.holo_green_light),
            ContextCompat.getColor(this, android.R.color.holo_orange_light),
            ContextCompat.getColor(this, android.R.color.holo_purple),
        )
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
                            colors[colorIndex]
                        )
                    )
                    it.setBackgroundColor(colors[colorIndex])
                    colorIndex = (colorIndex + 1) % colors.size
                }
                handler.postDelayed(this, 500.toLong())
            }
        }

        handler.post(changeColor)
    }

    private fun setButtonsClickEvent() {
        buttons.forEach {
            it.setOnClickListener { button ->
                Log.d(
                    "MainActivity",
                    "setButtonsClickEvent ${button.id} - with color: " +
                            "${(button.background as ColorDrawable).color} clicked"
                )
                handler.post {
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

                if (button.id == R.id.button1) {
                    Log.d("MainActivity", "setButtonsClickEvent: button1 clicked")
                    handler.post {
                        val backImg = findViewById<View>(R.id.imageView)
                        if (backImg.visibility == View.VISIBLE) {
                            val alphaAnimation = ValueAnimator.ofFloat(1f, 0f)
                            alphaAnimation.duration = 500
                            alphaAnimation.addUpdateListener { animator ->
                                backImg.alpha = animator.animatedValue as Float
                                if (backImg.alpha == 0f) {
                                    backImg.visibility = View.GONE
                                }
                            }
                            alphaAnimation.start()
                        } else {
                            backImg.alpha = 0f
                            backImg.visibility = View.VISIBLE
                            val alphaAnimation = ValueAnimator.ofFloat(0f, 1f)
                            alphaAnimation.duration = 500
                            alphaAnimation.addUpdateListener { animator ->
                                backImg.alpha = animator.animatedValue as Float
                            }
                            alphaAnimation.start()
                        }
                    }

                }
            }
        }
    }

    override fun onResume() {
        Log.d("MainActivity", "onResume")
        if (isChangingColor) {
            startChangeColor()
            findViewById<Button>(R.id.startButton).text = "Stop"
        }
        val elapsedTime = System.currentTimeMillis() - startTime
        findViewById<TextView>(R.id.loadingTimeTextView).text = "Loading time: $elapsedTime ms"
        super.onResume()
    }

    override fun onPause() {
        Log.d("MainActivity", "onPause")
        handler.removeCallbacksAndMessages(null)

        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("MainActivity", "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        outState.putInt("colorIndex", colorIndex)
        outState.putBoolean("isChangingColor", isChangingColor)
    }

    // This method is called only when the activity is re-created
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d("MainActivity", "onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        colorIndex = savedInstanceState.getInt("colorIndex")
        isChangingColor = savedInstanceState.getBoolean("isChangingColor")
        if (isChangingColor) {
            startChangeColor()
            findViewById<Button>(R.id.startButton).text = "Stop"
        }
    }
}
