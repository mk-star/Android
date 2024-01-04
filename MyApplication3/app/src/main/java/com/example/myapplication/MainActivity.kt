package com.example.myapplication

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.data.DetailEvent
import com.example.myapplication.data.Event
import com.example.myapplication.data.EventDao
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.DetailItemBinding
import com.example.myapplication.databinding.MapViewBinding
import com.example.myapplication.network.NetworkManager
import com.example.myapplication.ui.EventAdapter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.coroutineScope
import java.util.Locale
import android.util.Log

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    val REQ_ADD = 100
    val REQ_UPDATE = 200
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    lateinit var adapter : EventAdapter
    lateinit var networkDao : NetworkManager
    private lateinit var currentLoc : Location

    lateinit var events : ArrayList<Event>
    lateinit var detailEvent: DetailEvent
    lateinit var eventDao: EventDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        networkDao = NetworkManager(this)
        eventDao = EventDao(this)

        adapter = EventAdapter()
        mainBinding.eventInfo.adapter = adapter
        mainBinding.eventInfo.layoutManager = LinearLayoutManager(this)

        events = eventDao.getAllEvents()

        CoroutineScope(Dispatchers.Main).launch{
            val def = async(Dispatchers.IO) {
                var events : List<Event>? = null
                try {
                    events = networkDao.downloadXml()
                } catch (e: IOException) {
                    Log.d(TAG, e.message?: "null")
                    null
                } catch (e: XmlPullParserException) {
                    Log.d(TAG, e.message?: "null")
                    null
                }
                events
            }

            val eventList = def.await()
            if (eventList != null) {
                for (event in eventList) {
                    Log.d(TAG, "Event: $event")
                    eventDao.insertEvent(event)
                }
                events = ArrayList(eventList) // events 리스트 초기화
                adapter.events = eventList
                // 화면 갱신
                adapter.notifyDataSetChanged()

                // gpsX와 gpsY를 사용하는 예시 코드

            }
        }

        val onClickListener = object : EventAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.d(TAG, events.size.toString())
                /*클릭 항목의 dto 를 intent에 저장 후 UpdateActivity 실행*/
                val intent = Intent(this@MainActivity, EventDetailActivity::class.java)
                intent.putExtra("seq", events.get(position).seq)
                startActivityForResult(intent, REQ_UPDATE)
            }
        }
        adapter.setOnItemClickListener(onClickListener)

        mainBinding.btnReview1.setOnClickListener {
            val intent = Intent(this, ShowReviewActivity::class.java)
            startActivity(intent)
        }
    }
}