package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var startTime = 0L
    private var timeInMilliseconds = 0L
    private var timeSwapBuff = 0L
    private var updateTime = 0L
    private var isRunning = false

    private val handler = Handler()

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            updateTime = timeSwapBuff + timeInMilliseconds
            val secs = (updateTime / 1000).toInt()
            val mins = secs / 60
            val hrs = mins / 60
            val milliseconds = (updateTime % 1000).toInt()
            binding.tvtimer.text = String.format("%02d:%02d:%02d.%03d", hrs, mins % 60, secs % 60, milliseconds)
            handler.postDelayed(this, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartPause.setOnClickListener {
            if (isRunning) {
                timeSwapBuff += timeInMilliseconds
                handler.removeCallbacks(updateTimerThread)
                binding.btnStartPause.text = "Start"
                isRunning = false
            } else {
                startTime = SystemClock.uptimeMillis()
                handler.postDelayed(updateTimerThread, 0)
                binding.btnStartPause.text = "Pause"
                isRunning = true
            }
        }

        binding.btnReset.setOnClickListener {
            startTime = 0L
            timeInMilliseconds = 0L
            timeSwapBuff = 0L
            updateTime = 0L
            handler.removeCallbacks(updateTimerThread)
            binding.tvtimer.text = "00:00:00.000"
            binding.btnStartPause.text = "Start"
            isRunning = false
        }
    }
}
