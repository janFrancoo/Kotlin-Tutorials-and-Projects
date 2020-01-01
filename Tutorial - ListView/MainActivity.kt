package com.janfranco.kotlinlistview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val landmarkList = ArrayList<Landmark>()
        landmarkList.add(Landmark("Pisa", R.drawable.pisa))

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, landmarkList)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position,
                                                                         id ->
            val intent = Intent(applicationContext, DetailActivity::class.java)
            intent.putExtra("name", landmarkList[position].name)
            intent.putExtra("img_id", landmarkList[position].imgId)
            startActivity(intent)
        }

    }

}
