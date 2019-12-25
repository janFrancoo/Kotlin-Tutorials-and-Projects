package com.janfranco.kotlinartbook

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var db : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val idArrList = ArrayList<Int>()
        val nameArrList = ArrayList<String>()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nameArrList)
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
                val intentToDetail = Intent(this, AddActivity::class.java)
            intentToDetail.putExtra("artID", idArrList[position])
            intentToDetail.putExtra("info", "view")
            startActivity(intentToDetail)
        }

        db = this.openOrCreateDatabase("Arts", Context.MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS arts (artID INTEGER PRIMARY KEY, artName VARCHAR, artIMG BLOB)")

        val cursor = db.rawQuery("SELECT * FROM arts", null)
        val idIdx = cursor.getColumnIndex("artID")
        val nameIdx = cursor.getColumnIndex("artName")

        while (cursor.moveToNext()) {
            idArrList.add(cursor.getInt(idIdx))
            nameArrList.add(cursor.getString(nameIdx))
        }

        adapter.notifyDataSetChanged()
        cursor.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_art) {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}
