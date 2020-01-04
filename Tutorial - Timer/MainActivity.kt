package com.janfranco.kotlintutorialtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        object : CountDownTimer(10000, 1000) {
            override fun onFinish() {
                textView.text = "Finished!"
            }

            override fun onTick(millisUntilFinished: Long) {
                val sb = StringBuilder()
                sb.append("Left = ").append(millisUntilFinished/1000)
                textView.text = sb.toString()
            }
        }.start()

    }
}
