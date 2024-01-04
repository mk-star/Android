package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.MapViewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.StyleSpan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class MapActivity: AppCompatActivity() {
    private val TAG = "MapActivity"

    val mapViewBinding by lazy {
        MapViewBinding.inflate(layoutInflater)
    }

    private lateinit var geocoder : Geocoder
    lateinit var googleMap: GoogleMap
    private lateinit var targetLoc : LatLng
    var centerMarker : Marker? = null
    var gpsX: Double = 0.0
    var gpsY: Double = 0.0
    var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mapViewBinding.root)

        geocoder = Geocoder(this, Locale.getDefault())

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync (mapReadyCallback)
    }
    val mapReadyCallback = object : OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
            Log.d(TAG, "GoogleMap is ready")

            gpsX = intent.getDoubleExtra("gpsX", 0.0)
            gpsY = intent.getDoubleExtra("gpsY", 0.0)
            title = Html.fromHtml(intent.getStringExtra("title")).toString()

            Log.d(TAG, "Received GPS Coordinates: $gpsX, $gpsY")
            targetLoc = LatLng(gpsY, gpsX)
            Log.d(TAG, "Target Location: $targetLoc")
            addMarker(targetLoc)

            googleMap.setOnMapLoadedCallback {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLoc, 17f))
            }

        }
    }

    fun addMarker(targetLoc: LatLng) {  // LatLng(37.606320, 127.041808)
        Log.d(TAG, "Target Location in addMarker: $targetLoc") // 확인용 로그
        val markerOptions: MarkerOptions = MarkerOptions()
        markerOptions.position(targetLoc)
            .title(title)
        centerMarker = googleMap.addMarker(markerOptions)
        centerMarker?.showInfoWindow()
    }
}