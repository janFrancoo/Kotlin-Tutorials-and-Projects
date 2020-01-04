package com.janfranco.kotlintutorialrunnable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    var number = 0
    var runnable : Runnable = Runnable {  }
    var handler : Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun start(@Suppress("UNUSED_PARAMETER")view: View) {
        number = 0
        runnable = object : Runnable {
            override fun run() {
                number++
                val sb = StringBuilder()
                sb.append("TIME = ").append(number)
                textView.text = sb.toString()
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    fun stop(@Suppress("UNUSED_PARAMETER")view : View) {
        handler.removeCallbacks(runnable)
        number = 0
        val sb = StringBuilder()
        sb.append("TIME = ").append(number)
        textView.text = sb.toString()
    }
}
