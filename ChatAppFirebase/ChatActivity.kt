package com.janfranco.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private val userList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        getUsers()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userList)
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            val intentToDetail = Intent(this, ChatDetail::class.java)
            intentToDetail.putExtra("user", userList[position])
            startActivity(intentToDetail)
        }
    }

    private fun getUsers() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        val db = FirebaseFirestore.getInstance()
        db.collection("users").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(this, exception.localizedMessage.toString(),
                    Toast.LENGTH_LONG).show()
            } else {
                if (snapshot != null) {
                    userList.clear()
                    val docs = snapshot.documents
                    for (doc in docs) {
                        if (doc.get("username") != currentUser!!.email) {
                            userList.add(doc.get("username") as String)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_logout) {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
