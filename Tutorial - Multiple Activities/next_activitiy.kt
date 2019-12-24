package com.janfranco.kotlintutorialactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_next.*
import java.lang.StringBuilder

class NextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        val intent = intent
        val name = intent.getStringExtra("name")
        val surname = intent.getStringExtra("surname")

        val sb = StringBuilder()
        sb.append("Name = ").append(name).append("\nSurname = ").append(surname)
        textView.text = sb.toString()
    }
}
