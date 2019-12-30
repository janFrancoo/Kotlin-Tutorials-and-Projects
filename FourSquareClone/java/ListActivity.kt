package com.janfranco.kotlinfoursquareparse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val nameArrList = ArrayList<String>()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nameArrList)
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            val intentToDetail = Intent(this, DetailActivity::class.java)
            intentToDetail.putExtra("placeName", nameArrList[position])
            startActivity(intentToDetail)
        }

        val query = ParseQuery.getQuery<ParseObject>("Places")
        query.findInBackground { objects, e ->
            if (e != null)
                Toast.makeText(this, e.localizedMessage?.toString(),
                    Toast.LENGTH_LONG).show()
            else if (objects.size > 0 ) {
                nameArrList.clear()
                for (parseObj in objects) {
                    nameArrList.add(parseObj.get("name") as String)
                }
                adapter.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.opt_logout) {
            ParseUser.logOutInBackground { e: ParseException? ->
                if (e != null)
                    Toast.makeText(this, e.localizedMessage?.toString(),
                        Toast.LENGTH_LONG).show()
                else {
                    Toast.makeText(this, "Good bye!", Toast.LENGTH_LONG).show()
                    val intentToLogin = Intent(this, LoginActivity::class.java)
                    startActivity(intentToLogin)
                }
            }
        } else if(item.itemId == R.id.opt_add_place) {
            val intentToAddPlace = Intent(this, AddPlaceActivity::class.java)
            startActivity(intentToAddPlace)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
