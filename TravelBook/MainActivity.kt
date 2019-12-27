package com.janfranco.kotlintravelbook

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

        val placeArrList = ArrayList<Place>()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, placeArrList)
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            val intentToDetail = Intent(this, MapsActivity::class.java)
            intentToDetail.putExtra("id", placeArrList[position].placeId)
            startActivity(intentToDetail)
        }

        db = this.openOrCreateDatabase("Places", Context.MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS places (id INTEGER PRIMARY KEY, " +
                "name VARCHAR, latitude DOUBLE, longitude DOUBLE)")

        val cursor = db.rawQuery("SELECT * FROM places", null)
        val idIdx = cursor.getColumnIndex("id")
        val nameIdx = cursor.getColumnIndex("name")
        val latitudeIdx = cursor.getColumnIndex("latitude")
        val longitudeIdx = cursor.getColumnIndex("longitude")

        while (cursor.moveToNext()) {
            placeArrList.add(Place(cursor.getInt(idIdx), cursor.getString(nameIdx),
                cursor.getDouble(latitudeIdx), cursor.getDouble(longitudeIdx)))
        }

        adapter.notifyDataSetChanged()
        cursor.close()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_place, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_place_item) {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("info", "create")
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
