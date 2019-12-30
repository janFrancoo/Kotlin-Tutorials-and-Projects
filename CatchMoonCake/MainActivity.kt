package com.janfranco.kotlincatchmooncake

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import java.util.*

class MainActivity : AppCompatActivity() {

    private var score = 0
    private var timeLeft = 10
    private var maxScore : Int? = null
    private var runnable : Runnable = Runnable {  }
    private var runnable2 : Runnable = Runnable {  }
    private val handler : Handler = Handler()
    private val random : Random = Random()
    private var sharedPreferences : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences(
            "com.janfranco.kotlincatchmooncake", Context.MODE_PRIVATE)
        maxScore = sharedPreferences!!.getInt("max_score", 0)
        val sb = StringBuilder()
        sb.append("Score = ").append(score).append(" | Max Score = ").append(maxScore!!)
        textView.text = sb.toString()
        runnable = object : Runnable {
            override fun run() {
                timeLeft--
                val sb = StringBuilder()
                sb.append("TIME LEFT = ").append(timeLeft)
                textView2.text = sb.toString()
                handler.postDelayed(this, 1000)
                if(timeLeft == 0)
                    handler.removeCallbacks(this)
            }
        }
        runnable2 = object : Runnable {
            override fun run() {
                imageView.x = random.nextInt(800).toFloat()
                imageView.y = random.nextInt(1200).toFloat()
                handler.postDelayed(this, random.nextInt(700).toLong() + 300)
                if(timeLeft == 0)
                    handler.removeCallbacks(runnable2)
            }

        }
        handler.post(runnable)
        handler.post(runnable2)
    }

    fun hit(@Suppress("UNUSED_PARAMETER")view : View) {
        if(timeLeft > 0) {
            score++
            if(score > maxScore!!)
                maxScore = score
            val sb = StringBuilder()
            sb.append("Score = ").append(score).append(" | Max Score = ").append(maxScore!!)
            textView.text = sb.toString()
        }
        else {
            sharedPreferences!!.edit().putInt("max_score", maxScore!!).apply()
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Game Over")
            alert.setMessage("Do you want to play again?")
            alert.setPositiveButton("Yes") {dialog, which ->
                timeLeft = 10
                score = 0
                val sb = StringBuilder()
                sb.append("Score = ").append(score).append(" | Max Score = ").append(maxScore!!)
                textView.text = sb.toString()
                sb.clear()
                sb.append("TIME LEFT = ").append(timeLeft)
                textView2.text = sb.toString()
                handler.post(runnable)
                handler.post(runnable2)
            }
            alert.setNegativeButton("No") {dialog, which ->
                finish()
            }
            alert.show()
        }
    }
}
