package com.janfranco.kotlinfirebaseinsta

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_upload.*
import java.util.*

class UploadActivity : AppCompatActivity() {

    private var selectedPicture : Uri? = null
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    fun selectImg(@Suppress("UNUSED_PARAMETER")view : View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentToGallery, 2)
        }
    }

    fun uploadImg(@Suppress("UNUSED_PARAMETER")view : View) {
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        val storage = FirebaseStorage.getInstance()
        val reference = storage.reference
        val imagesReference = reference.child("images").child(imageName)
        if (selectedPicture != null)
            imagesReference.putFile(selectedPicture!!).addOnSuccessListener { taskSnapshot ->
                val uploadedPicRef = FirebaseStorage.getInstance().reference.child("images").child(imageName)
                uploadedPicRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    val postMap = hashMapOf<String, Any>()
                    postMap["downloadUrl"] = downloadUrl
                    postMap["userEmail"] = auth.currentUser!!.email.toString()
                    postMap["comment"] = editText.text.toString()
                    postMap["date"] = Timestamp.now()
                    db.collection("Posts").add(postMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            finish()
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(this, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentToGallery, 2)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPicture = data.data
            if (Build.VERSION.SDK_INT >= 28) {
                val source = ImageDecoder.createSource(contentResolver, selectedPicture!!)
                val bitmap = ImageDecoder.decodeBitmap(source)
                imageView.setImageBitmap(bitmap)
            } else {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
                imageView.setImageBitmap(bitmap)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
