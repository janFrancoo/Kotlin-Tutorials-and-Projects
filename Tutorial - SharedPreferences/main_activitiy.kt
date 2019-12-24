package com.janfranco.tutorialkotlinsharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences(
            "com.janfranco.tutorialkotlinsharedpreferences", Context.MODE_PRIVATE)
        val intFromSharedPreferences = sharedPreferences.getInt("age", -1)
        if (intFromSharedPreferences != -1) {
            val sb = StringBuilder()
            sb.append("Your age = ").append(intFromSharedPreferences)
            textView.text = sb.toString()
        }
    }

    fun save (@Suppress("UNUSED_PARAMETER")view : View) {

        val age = editText.text.toString().toIntOrNull()
        if (age != null) {
            val sb = StringBuilder()
            sb.append("Your age = ").append(age)
            textView.text = sb.toString()
            sharedPreferences.edit().putInt("age", age).apply()
        }

    }

    fun delete (@Suppress("UNUSED_PARAMETER")view : View) {

        val intFromSharedPreferences = sharedPreferences.getInt("age", -1)
        if (intFromSharedPreferences != -1) {
            sharedPreferences.edit().remove("age").apply()
            textView.text = "Age = "
        }

    }
}
