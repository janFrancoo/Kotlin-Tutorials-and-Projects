package com.janfranco.kotlinfoursquareparse

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.parse.ParseFile
import com.parse.ParseObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener : LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(longClickListener)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                if (location != null) {
                    mMap.clear()
                    val userLocation = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                }
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) { }
            override fun onProviderEnabled(provider: String?) { }
            override fun onProviderDisabled(provider: String?) { }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        else
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1, 1f, locationListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1 && grantResults.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1, 1f, locationListener)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val longClickListener =
        GoogleMap.OnMapLongClickListener { p0 ->
            if (p0 != null) {
                val placeName = intent.getStringExtra("placeName") as String
                val placeImg = intent.getByteArrayExtra("placeImg") as ByteArray

                val parseFile = ParseFile("image.png", placeImg)
                val parseObj = ParseObject("Places")
                parseObj.put("name", placeName)
                parseObj.put("image", parseFile)
                parseObj.put("latitude", p0.latitude)
                parseObj.put("longitude", p0.longitude)
                parseObj.saveInBackground { e ->
                    if (e != null)
                        Toast.makeText(this, e.localizedMessage?.toString(),
                            Toast.LENGTH_LONG).show()
                    else {
                        Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show()
                        val intentToList = Intent(this, ListActivity::class.java)
                        intentToList.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intentToList)
                        finish()
                    }
                }
            }
        }
}
