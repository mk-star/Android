package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.EventDao
import com.example.myapplication.databinding.ShowReviewBinding
import com.example.myapplication.ui.EventAdapter
import com.example.myapplication.ui.ReviewAdapter

class ShowReviewActivity: AppCompatActivity() {
    private val TAG = "MapActivity"
    lateinit var eventDao: EventDao
    val REQ_UPDATE = 200
    lateinit var adapter : ReviewAdapter

    val showReviewBinding by lazy {
        ShowReviewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(showReviewBinding.root)

        eventDao = EventDao(this)
        adapter = ReviewAdapter()
        showReviewBinding.reviewInfo.adapter = adapter
        showReviewBinding.reviewInfo.layoutManager = LinearLayoutManager(this)

        Log.d("뜨긴해요??", "앙????")
        var reviews = eventDao.getAllReviews()
        adapter.reviews = reviews
        adapter.notifyDataSetChanged()

        Log.d("개수뜨니?", reviews.toString())

        val onClickListener = object : ReviewAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.d(TAG, reviews.size.toString())
                /*클릭 항목의 dto 를 intent에 저장 후 UpdateActivity 실행*/
                val intent = Intent(this@ShowReviewActivity, EventDetailActivity::class.java)
                intent.putExtra("seq", reviews.get(position).reviewSeq)
                startActivityForResult(intent, REQ_UPDATE)
            }
        }
        val deleteClickListener = object : ReviewAdapter.OnItemDeleteClickListener {
            override fun onItemDeleteClick(position: Int) {
                // 해당 아이템 삭제 처리
                val review = adapter.reviews?.get(position)
                review?.let { eventDao.deleteReview(it) }

                // 어댑터 갱신
                adapter.removeItem(position)
            }
        }
        adapter.setOnItemClickListener(onClickListener)
        adapter.setOnItemDeleteClickListener(deleteClickListener)

        showReviewBinding.btnHome2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}