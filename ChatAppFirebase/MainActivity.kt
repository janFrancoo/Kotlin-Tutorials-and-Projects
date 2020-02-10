package com.janfranco.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun signIn(view: View) {
        val email = emailText.text.toString()
        val pass = passText.text.toString()
        if (email != "" && pass != "") {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Welcome, ${auth.currentUser!!.email.toString()}",
                        Toast.LENGTH_LONG).show()
                    val intent = Intent(this, ChatActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, exception.localizedMessage.toString(),
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    fun signUp(view: View) {
        val email = emailText.text.toString()
        val pass = passText.text.toString()
        if (email != "" && pass != "") {
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val db = FirebaseFirestore.getInstance()
                    val user = hashMapOf("username" to email)

                    db.collection("users").add(user).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, ChatActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, exception.localizedMessage.toString(),
                    Toast.LENGTH_LONG).show()
            }
        }
    }

}
