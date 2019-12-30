package com.janfranco.kotlinfoursquareparse

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.android.synthetic.main.activity_detail.*

@Suppress("NAME_SHADOWING")
class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val name = intent.getStringExtra("placeName")
        val query = ParseQuery.getQuery<ParseObject>("Places")
        query.whereEqualTo("name", name)
        query.findInBackground { objects, e ->
            if (e != null)
                Toast.makeText(this, e.localizedMessage?.toString(),
                    Toast.LENGTH_LONG).show()
            else if (objects.size > 0 ) {
                for (parseObj in objects) {
                    val img = parseObj.get("image") as ParseFile
                    img.getDataInBackground { data, e ->
                        if (e != null)
                            Toast.makeText(this, e.localizedMessage?.toString(),
                                Toast.LENGTH_LONG).show()
                        else {
                            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                            imageView.setImageBitmap(bitmap)
                            textView.text = name
                            val selectedPosition = LatLng(parseObj.get("latitude") as Double,
                                parseObj.get("longitude") as Double)
                            mMap.addMarker(MarkerOptions().
                                position(selectedPosition).title("Your Place"))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                selectedPosition, 15f))
                        }
                    }
                }
            }
        }

    }


}
