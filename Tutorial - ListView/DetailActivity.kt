package com.janfranco.kotlinlistview

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val intent = intent

        val name = intent.getStringExtra("name")
        textView.text = name

        val imgId = intent.getIntExtra("img_id", 0)
        val selectedBitmap = BitmapFactory.decodeResource(applicationContext.resources, imgId)
        imageView.setImageBitmap(selectedBitmap)
    }
}
