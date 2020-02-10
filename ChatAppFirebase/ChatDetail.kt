package com.janfranco.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chat_detail.*

class ChatDetail : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private lateinit var currentUser: FirebaseUser
    private lateinit var user: String

    private var postArr = ArrayList<String>()
    private lateinit var adapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)

        currentUser = auth.currentUser!!
        user = intent.getStringExtra("user") as String

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, postArr)
        postListView.adapter = adapter

        getChat()
    }

    private fun getChat() {
        db.collection("posts").whereEqualTo("user1", currentUser.email)
            .whereEqualTo("user2", user).addSnapshotListener { snapshot, exception ->
            if (exception != null)
                Toast.makeText(this, exception.localizedMessage.toString(),
                    Toast.LENGTH_LONG).show()
            else {
                if (snapshot != null && !snapshot.isEmpty) {
                    postArr.clear()
                    for (document in snapshot.documents) {
                        val arr = document.get("post_array") as ArrayList<String>
                        for (post in arr) {
                            postArr.add(post)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                else {
                    db.collection("posts").whereEqualTo("user1", user)
                        .whereEqualTo("user2", currentUser.email)
                        .addSnapshotListener { snapshot, exception ->
                            if (exception != null) {
                                Toast.makeText(this, exception.localizedMessage.toString(),
                                    Toast.LENGTH_LONG).show()
                            }
                            else {
                                if (snapshot != null && !snapshot.isEmpty) {
                                    postArr.clear()
                                    for (document in snapshot.documents) {
                                        val arr = document.get("post_array") as ArrayList<String>
                                        for (post in arr) {
                                            postArr.add(post)
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

    fun send(view: View) {
        db.collection("posts").whereEqualTo("user1", currentUser.email)
            .whereEqualTo("user2", user).addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(this, exception.localizedMessage.toString(),
                        Toast.LENGTH_LONG).show()
                }
                else {
                    if (snapshot != null && !snapshot.isEmpty) {
                        for (document in snapshot.documents) {
                            val documentId = document.id
                            db.collection("posts").document(documentId)
                                .update("post_array",
                                    FieldValue.arrayUnion(messageText.text.toString()))
                                .addOnCompleteListener { task ->
                                    messageText.setText("")
                                }
                        }
                    }
                    else {
                        db.collection("posts").whereEqualTo("user1", user)
                            .whereEqualTo("user2", currentUser.email)
                            .addSnapshotListener { snapshot, exception ->
                                if (exception == null && snapshot != null && !snapshot.isEmpty) {
                                    for (document in snapshot.documents) {
                                        val documentId = document.id
                                        db.collection("posts").document(documentId)
                                            .update("post_array",
                                                FieldValue.arrayUnion(messageText.text.toString()))
                                            .addOnCompleteListener { task ->
                                                messageText.setText("")
                                            }
                                    }
                                }
                                else {
                                    val postMap = hashMapOf<String, Any?>()
                                    postMap["user1"] = currentUser!!.email
                                    postMap["user2"] = user
                                    val postList = ArrayList<String>()
                                    postList.add(messageText.text.toString())
                                    postMap["post_array"] = postList
                                    db.collection("posts").add(postMap).addOnFailureListener { exception ->
                                        Toast.makeText(this, exception.localizedMessage.toString(),
                                            Toast.LENGTH_LONG).show()
                                    }.addOnCompleteListener { task ->
                                        messageText.setText("")
                                    }
                                }
                            }
                    }
                }
            }
    }
}
