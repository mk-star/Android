package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.myapplication.data.DetailEvent
import com.example.myapplication.data.Event
import com.example.myapplication.data.EventDao
import com.example.myapplication.data.Review
import com.example.myapplication.databinding.DetailItemBinding
import com.example.myapplication.databinding.MapViewBinding
import com.example.myapplication.network.DetailNetworkManager
import com.example.myapplication.ui.DetailEventAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class EventDetailActivity  : AppCompatActivity() {
    private val TAG = "EventDetailActivity"
    val detailItemBinding by lazy {
        DetailItemBinding.inflate(layoutInflater)
    }
    val mapViewBinding by lazy {
        MapViewBinding.inflate(layoutInflater)
    }

    lateinit var eventDao: EventDao
    lateinit var networkDao : DetailNetworkManager
    lateinit var review: Review

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailItemBinding.root)
        var detailEvent: DetailEvent? = null
        eventDao = EventDao(this)
        networkDao = DetailNetworkManager(this)


        /*RecyclerView 에서 선택하여 전달한 dto 를 확인*/
        val seq = intent.getSerializableExtra("seq") as Int

        Log.d("흐우ㅜㅜ", seq.toString())

        CoroutineScope(Dispatchers.Main).launch {
            val def = async(Dispatchers.IO) {
                var detailEvent: DetailEvent? = null
                try {
                    detailEvent = networkDao.downloadXml(seq)
                } catch (e: IOException) {
                    Log.d(TAG, e.message ?: "null")
                    null
                } catch (e: XmlPullParserException) {
                    Log.d(TAG, e.message ?: "null")
                    null
                }
                detailEvent
            }

            detailEvent = def.await()

            Glide.with(applicationContext)
                .load(detailEvent?.imgUrl)
                .into(detailItemBinding.detailImage)

            Log.d(TAG, detailEvent?.gpsX.toString())
            Log.d(TAG, detailEvent?.gpsY.toString())


            val startYear = detailEvent?.startDate.toString().substring(0, 4)
            val startMonth = detailEvent?.startDate.toString().substring(4, 6)
            val startDay = detailEvent?.startDate.toString().substring(6, 8)

            val startD = "${startYear}년 ${startMonth}월 ${startDay}일"

            val endYear = detailEvent?.endDate.toString().substring(0, 4)
            val endMonth = detailEvent?.endDate.toString().substring(4, 6)
            val endDay = detailEvent?.endDate.toString().substring(6, 8)

            val endD = "${endYear}년 ${endMonth}월 ${endDay}일"

            detailItemBinding.detailTitle.setText("제목: " +  (Html.fromHtml(detailEvent?.title)))
            detailItemBinding.detailStartDate.setText("시작일: " + startD)
            detailItemBinding.detailEndDate.setText("마감일: " + endD)
            detailItemBinding.detailPlace.setText("장소: " + detailEvent?.place)
            detailItemBinding.detailPrice.setText("티켓 요금: " + detailEvent?.price.toString())

            detailItemBinding.btnMap.visibility = if (detailEvent?.gpsX != null && detailEvent?.gpsY != null) {
                View.VISIBLE // 값이 있으면 버튼을 보이게 함
            } else {
                View.GONE // 값이 없으면 버튼을 숨김
            }
        }

        detailItemBinding.btnMap.setOnClickListener {
            val intent = Intent(this@EventDetailActivity, MapActivity::class.java)
            intent.putExtra("gpsX", detailEvent?.gpsX)
            intent.putExtra("gpsY", detailEvent?.gpsY)
            intent.putExtra("title", detailEvent?.title)
            startActivity(intent)
        }

        detailItemBinding.btnWriteRv.setOnClickListener {
            val intent = Intent(this@EventDetailActivity, WriteReviewActivity::class.java)
            intent.putExtra("reviewSeq", detailEvent?.seq)
            intent.putExtra("title", detailEvent?.title)
            intent.putExtra("img", detailEvent?.imgUrl)
            startActivity(intent)
        }
    }

}