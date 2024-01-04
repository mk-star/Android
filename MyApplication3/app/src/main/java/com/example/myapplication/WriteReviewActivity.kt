package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.data.Event
import com.example.myapplication.data.EventDao
import com.example.myapplication.data.Review
import com.example.myapplication.databinding.ShowReviewBinding
import com.example.myapplication.databinding.WriteReviewBinding

class WriteReviewActivity: AppCompatActivity() {
    private val TAG = "WriteReviewActivity"
    lateinit var eventDao: EventDao

    val writeReviewBinding by lazy {
        WriteReviewBinding.inflate(layoutInflater)
    }
    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(writeReviewBinding.root)

        eventDao = EventDao(this)

        var seq = intent.getIntExtra("reviewSeq", 0)
        var title = intent.getStringExtra("title")
        var img = intent.getStringExtra("img")

        Log.d("title", title.toString())
        Log.d("img", img.toString())
        var existingReview: Review = eventDao.getReviewByEventSeq(seq)

        Log.d("ExistingReview", "ReviewSeq: ${existingReview.reviewSeq}, ReviewText: ${existingReview.reviewText}, ReviewTitle: ${existingReview.reviewTitle}, ReviewImage: ${existingReview.reviewImage},")


        writeReviewBinding.etReview.setText(existingReview.reviewText)
        Glide.with(this)
            .load(img)
            .into(writeReviewBinding.rvImage)

        writeReviewBinding.btnAddRv.setOnClickListener {
            val reviewText = writeReviewBinding.etReview.text.toString()
            val reviewTitle = existingReview.reviewTitle
            val reviewImage = existingReview.reviewImage

            if (existingReview.reviewSeq != null) {
                Log.d("여기야?", seq.toString())
                existingReview.reviewText = reviewText
                eventDao.updateReview(existingReview)
            } else {
                val newReview = Review(seq, reviewText, title, img)
                Log.d("여기죠?", seq.toString())
                eventDao.insertReview(newReview)
            }
            finish()
        }
    }

    //@SuppressLint("Range")
    //private fun searchImages(seq: Int?) : List<MediaDto> {
    //    val imageUri =  MediaStore.Images.Media.EXTERNAL_CONTENT_URI       // 외부저장소 이미지 대상
    //
    //    val projection = arrayOf(
    //        MediaStore.Images.Media._ID,    // ID
     //       MediaStore.Images.Media.DISPLAY_NAME,   // 파일명
     //       MediaStore.Images.Media.DATA,       // 전체경로
    //    )
    //    val selection = MediaStore.Images.Media.MIME_TYPE + "=?"    // 이미지 유형 지정
    //    val selectArgs = arrayOf("image/jpeg")      // jpg 이미지 (image/png)
//
   //     var list = arrayListOf<MediaDto>()
//
        /*contentResolver 를 사용하여 외부저장소의 이미지 확인*/
 //       val cursor : Cursor = applicationContext.contentResolver.query(
  //          imageUri, null, null, null, null
  //      ) ?: return list

        /*Images 의 각 컬럼 인덱스를 확인하여 cursor 에서 데이터 확인*/
   //     with (cursor) {
  //          while (moveToNext()) {
  ///             val id = getLong(getColumnIndex(MediaStore.Images.Media._ID))
    //            val fileName = getString(getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
    //            val path = getString(getColumnIndex(MediaStore.Images.Media.DATA))
   //             list.add( MediaDto(id, seq, fileName, path))
  //          }
  //      }

    //    return list
 //   }
    fun checkPermissions () : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "The permission is already granted")
                return true
            } else {
                readImagePermissionRequest.launch(Manifest.permission.READ_MEDIA_IMAGES,)
                return false
            }
        }
        return true
    }
    /*registerForActivityResult 는 startActivityForResult() 대체*/
    val readImagePermissionRequest
            = registerForActivityResult( ActivityResultContracts.RequestPermission() ) {
            isGranted ->
        if (isGranted) {
            Log.d(TAG, "The Permission is granted")
        } else {
            Log.d(TAG, "The Permission is not granted")
        }
    }
}