package com.janfranco.kotlintutorialalertdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun save(@Suppress("UNUSED_PARAMETER")view : View) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Save")
        alert.setMessage("Are you sure?")
        alert.setPositiveButton("Yes") {dialog, which ->
            Toast.makeText(applicationContext, "Saved", Toast.LENGTH_LONG).show()
        }
        alert.setNegativeButton("No") {dialog, which ->
            Toast.makeText(applicationContext, "Not Saved", Toast.LENGTH_LONG).show()
        }
        alert.show()
    }
}
