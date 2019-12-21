package com.janfranco.kotlincalculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var num1 : Int? = null
    var num2 : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun getValues() {
        num1 = editText.text.toString().toIntOrNull()
        num2 = editText2.text.toString().toIntOrNull()
    }

    private fun isNull() : Boolean {
        when(num1 == null || num2 == null) {
            true -> return true
            false -> return false
        }
    }

    fun add(view : View) {
        getValues()
        if(isNull())
            textView.setText("Error")
        else {
            textView.setText("Result = ${num1!! + num2!!}")
        }
    }

    fun subtract(view : View) {
        getValues()
        if(isNull())
            textView.setText("Error")
        else {
            textView.setText("Result = ${num1!! - num2!!}")
        }
    }

    fun multiply(view : View) {
        getValues()
        if(isNull())
            textView.setText("Error")
        else {
            textView.setText("Result = ${num1!! * num2!!}")
        }
    }

    fun divide(view : View) {
        getValues()
        if(isNull())
            textView.setText("Error")
        else {
            textView.setText("Result = ${num1!! / num2!!}")
        }
    }


}
