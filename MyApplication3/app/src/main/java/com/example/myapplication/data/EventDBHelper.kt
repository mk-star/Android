package com.example.myapplication.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

class EventDBHelper (context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1){
    val TAG = "EventDBHelpter"
    companion object {
        const val DB_NAME = "event_db"
        const val TABLE_NAME = "event_table"
        const val COL_SEQ = "seq"
        const val COL_TITLE = "title"
        const val COL_PHOTO = "thumbnail"
        const val  COL_GPSX = "gpsX"
        const val COL_GPSY = "gpsY"
        const val COL_STARTDATE = "startDate"
        const val COL_ENDDATE = "endDate"
        const val COL_PLACE = "place"
        const val COL_PRICE = "price"
        const val COL_RVSEQ = "reviewSeq"
        const val COL_RVTEXT = "reviewText"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME ($COL_SEQ INTEGER PRIMARY KEY, $COL_TITLE TEXT, $COL_PHOTO TEXT, $COL_GPSX DOUBLE, $COL_GPSY DOUBLE, " +
                    "$COL_STARTDATE INT, $COL_ENDDATE INT, $COL_PLACE TEXT, $COL_PRICE TEXT, $COL_RVSEQ INTEGER, $COL_RVTEXT TEXT)"
        Log.d(TAG, CREATE_TABLE)
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Version 1에서 Version 2로 업그레이드할 때 reviewText 컬럼 추가
            val ADD_REVIEW_COLUMN = "ALTER TABLE $TABLE_NAME ADD COLUMN $COL_RVTEXT TEXT"
            db?.execSQL(ADD_REVIEW_COLUMN)
        }
        // 다른 버전에 대한 업그레이드 로직을 추가할 수 있습니다.
        // onCreate 메서드를 호출하여 새로운 테이블을 생성합니다.
        onCreate(db)
    }


}