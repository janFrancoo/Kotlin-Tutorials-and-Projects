package com.janfranco.kotlintravelbook

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.location.Geocoder
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
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var info : String? = null
    private lateinit var mMap: GoogleMap
    private lateinit var db : SQLiteDatabase
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener : LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        info = intent.getStringExtra("info")
        db = this.openOrCreateDatabase("Places", Context.MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS places (id INTEGER PRIMARY KEY, " +
                "name VARCHAR, latitude DOUBLE, longitude DOUBLE)")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (info == "create") {
            mMap.setOnMapLongClickListener(myListener)
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

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                }

                override fun onProviderEnabled(provider: String?) {

                }

                override fun onProviderDisabled(provider: String?) {

                }
            }

        } else {
            val placeId = intent.getIntExtra("id", 0)
            val cursor = db.rawQuery("SELECT * FROM places WHERE id = ?", arrayOf(placeId.toString()), null)
            val nameIdx = cursor.getColumnIndex("name")
            val latitudeIdx = cursor.getColumnIndex("latitude")
            val longitudeIdx = cursor.getColumnIndex("longitude")

            while (cursor.moveToNext()) {
                mMap.clear()
                val position = LatLng(cursor.getDouble(latitudeIdx), cursor.getDouble(longitudeIdx))
                mMap.addMarker(MarkerOptions().position(position).title(cursor.getString(nameIdx)))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
            }

            cursor.close()

        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            if (info == "create")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1, 1f, locationListener)
        }
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

    private val myListener = GoogleMap.OnMapLongClickListener { p0 ->
        """
        var address = ""
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        if (p0 != null) {
            try {
                val addressList = geocoder.getFromLocation(p0.latitude, p0.longitude, 1)
                if (addressList != null && addressList.isNotEmpty()) {
                    if (addressList[0].thoroughfare != null) {
                        address += addressList[0].thoroughfare
                        if (addressList[0].subThoroughfare != null) {
                            address += addressList[0].subThoroughfare
                        }
                    } else {
                        address = "Unknown place"
                    }
                }
                val query = "INSERT INTO places (name, latitude, longitude) VALUES (?, ?, ?)"
                val statement = db.compileStatement(query)
                statement.bindString(1, address)
                statement.bindDouble(2, p0.latitude)
                statement.bindDouble(3, p0.longitude)
                statement.execute()
            } catch (e : Exception) {
                e.printStackTrace()
            }
            finish()
        } else {
            Toast.makeText(applicationContext, "Try Again!", Toast.LENGTH_LONG).show()
        }
        """
        val query = "INSERT INTO places (name, latitude, longitude) VALUES (?, ?, ?)"
        val statement = db.compileStatement(query)
        statement.bindString(1, "GeoCoder problem!")
        statement.bindDouble(2, p0.latitude)
        statement.bindDouble(3, p0.longitude)
        statement.execute()
        val intentToHome = Intent(this, MainActivity::class.java)
        intentToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentToHome);
    }

}
