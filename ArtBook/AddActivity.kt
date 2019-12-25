package com.janfranco.kotlinartbook

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_add.*
import java.io.ByteArrayOutputStream
import java.lang.Exception

class AddActivity : AppCompatActivity() {

    private lateinit var db : SQLiteDatabase
    private var selectedImage : Uri? = null
    private var selectedBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        db = this.openOrCreateDatabase("Arts", Context.MODE_PRIVATE, null)
        val intent = intent
        val info = intent.getStringExtra("info")
        if (info == "view") {
            imageView.isEnabled = false
            editText.isEnabled = false
            button.visibility = View.INVISIBLE

            val artID = intent.getIntExtra("artID", 0)

            try {
                val cursor = db.rawQuery(
                    "SELECT * FROM arts WHERE artID = ?",
                    arrayOf(artID.toString()), null
                )

                val nameIdx = cursor.getColumnIndex("artName")
                val imgIdx = cursor.getColumnIndex("artIMG")

                while(cursor.moveToNext()) {
                    val nameStr = cursor.getString(nameIdx)
                    editText.setText(nameStr)
                    println(nameStr)
                    val byteArr = cursor.getBlob(imgIdx)
                    val bitmap = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.size)
                    imageView.setImageBitmap(bitmap)
                }
                cursor.close()
            } catch (e : Exception) {
                println(e.localizedMessage)
            }

        }
    }

    fun selectImage(@Suppress("UNUSED_PARAMETER")view : View) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val intentToGallery = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentToGallery, 2)
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intentToGallery = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentToGallery, 2)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            selectedImage = data.data
            if (Build.VERSION.SDK_INT >= 28) {
                val source = ImageDecoder.createSource(this.contentResolver, selectedImage!!)
                selectedBitmap = ImageDecoder.decodeBitmap(source)
                imageView.setImageBitmap(selectedBitmap)
            } else {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,
                    selectedImage)
                imageView.setImageBitmap(selectedBitmap)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun decreaseImageSize(image : Bitmap, maximumSize : Int) : Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()
        if (bitmapRatio > 1) {
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        } else {
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image,width,height,true)
    }

    fun add(@Suppress("UNUSED_PARAMETER")view : View) {
        if (selectedBitmap != null) {
            val name = editText.text.toString()
            val smallImage = decreaseImageSize(selectedBitmap!!, 300)
            val outputStream = ByteArrayOutputStream()
            smallImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            val byteArr = outputStream.toByteArray()

            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS arts (artID INTEGER PRIMARY KEY, " +
                        "artName VARCHAR, artIMG BLOB)")
                val query = "INSERT INTO arts (artName, artIMG) VALUES (?, ?)"
                val statement = db.compileStatement(query)
                statement.bindString(1, name)
                statement.bindBlob(2, byteArr)
                statement.execute()
            } catch (e : Exception) {
                println(e.localizedMessage)
            }

            val intentToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentToMainActivity)
        }
    }

}
