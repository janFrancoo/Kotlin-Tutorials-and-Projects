package com.janfranco.kotlinfoursquareparse

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_add_place.*
import java.io.ByteArrayOutputStream

@Suppress("DEPRECATION")
class AddPlaceActivity : AppCompatActivity() {

    private var selectedImage : Uri? = null
    private var selectedBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)
    }

    fun selectImg(@Suppress("UNUSED_PARAMETER")view : View) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        else {
            val intentToGal = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentToGal, 2)
        }
    }

    fun next(@Suppress("UNUSED_PARAMETER")view : View) {
        if (selectedBitmap != null) {
            val smallImg = decreaseImageSize(selectedBitmap!!)
            val byteArrOStream = ByteArrayOutputStream()
            smallImg.compress(Bitmap.CompressFormat.PNG, 50, byteArrOStream)
            val bytes = byteArrOStream.toByteArray()

            val intentToMaps = Intent(this, MapsActivity::class.java)
            intentToMaps.putExtra("placeName", placeNameEdit.text.toString())
            intentToMaps.putExtra("placeImg", bytes)
            startActivity(intentToMaps)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intentToGal = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentToGal, 2)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            selectedImage = data.data
            if (Build.VERSION.SDK_INT >= 28) {
                val source = ImageDecoder.createSource(this.contentResolver, selectedImage!!)
                selectedBitmap = ImageDecoder.decodeBitmap(source)
                placeImg.setImageBitmap(selectedBitmap)
            } else {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,
                    selectedImage)
                placeImg.setImageBitmap(selectedBitmap)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun decreaseImageSize(image : Bitmap) : Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()
        if (bitmapRatio > 1) {
            width = 300
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        } else {
            height = 300
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image, width, height, true)
    }
}
