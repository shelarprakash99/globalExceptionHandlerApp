package com.example.globalexceptionhandlerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.globalexceptionhandlerlibrary.GlobalExceptionHandler

class MainActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalExceptionHandler.initialize(this,crashActivity::class.java)

        // Schedule throwing an exception after 5 seconds
        handler.postDelayed({
            throw RuntimeException("This is a custom exception.")
        }, 5000)

    }
}