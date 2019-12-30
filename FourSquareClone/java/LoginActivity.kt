package com.janfranco.kotlinfoursquareparse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.parse.ParseException
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user = ParseUser.getCurrentUser()
        if (user != null) {
            Toast.makeText(this, "Welcome ${user.username}", Toast.LENGTH_LONG).show()
            val intentToList = Intent(this, ListActivity::class.java)
            startActivity(intentToList)
            finish()
        }
    }

    fun signIn(@Suppress("UNUSED_PARAMETER")view : View) {
        ParseUser.logInInBackground(usernameEditText.text.toString(), passEditText.text.toString())
        { user, e ->
            if (e != null)
                Toast.makeText(this, e.localizedMessage?.toString(),
                    Toast.LENGTH_LONG).show()
            else {
                Toast.makeText(this, "Welcome ${user.username}",
                    Toast.LENGTH_LONG).show()
                val intentToList = Intent(this, ListActivity::class.java)
                startActivity(intentToList)
                finish()
            }
        }
    }

    fun signUp(@Suppress("UNUSED_PARAMETER")view : View) {
        val user = ParseUser()
        user.username = usernameEditText.text.toString()
        user.setPassword(passEditText.text.toString())
        user.signUpInBackground { e : ParseException? ->
            if (e != null)
                Toast.makeText(this, e.localizedMessage?.toString(),
                    Toast.LENGTH_LONG).show()
            else {
                Toast.makeText(this, "User created!", Toast.LENGTH_LONG).show()
                val intentToList = Intent(this, ListActivity::class.java)
                startActivity(intentToList)
                finish()
            }
        }
    }

}
