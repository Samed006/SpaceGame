package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class MainActivity: AppCompatActivity() {

    lateinit var canonView: CanonView
    lateinit var mainHandler: Handler

    private val updateTextTask = object : Runnable {
        override fun run() {
            shoot()
            mainHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        canonView = findViewById<CanonView>(R.id.vMain)
        mainHandler = Handler(Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()
        canonView.pause()
        mainHandler.removeCallbacks(updateTextTask)
    }

    override fun onResume() {
        super.onResume()
        canonView.resume()
        mainHandler.post(updateTextTask)
    }
    fun shoot(){
        canonView.fireball()
    }
}