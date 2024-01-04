package com.example.myapplication.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.myapplication.data.EventDBHelper.Companion.COL_RVSEQ
import com.example.myapplication.data.EventDBHelper.Companion.COL_SEQ
import com.example.myapplication.data.EventDBHelper.Companion.TABLE_NAME

class EventDao(val context: Context) {
    @SuppressLint("Range")
    fun getAllEvents() : ArrayList<Event> {
        val helper = EventDBHelper(context)
        val db = helper.readableDatabase

        val cursor = db.query(
            EventDBHelper.TABLE_NAME, null, null, null,
            null, null, null
        )

        val events = arrayListOf<Event>()
        with(cursor) {
            while (moveToNext()) {
                val seq = getInt(getColumnIndex(EventDBHelper.COL_SEQ))
                val title = getString(getColumnIndex(EventDBHelper.COL_TITLE))
                val thumbnail = getString(getColumnIndex(EventDBHelper.COL_PHOTO))
                val dto = Event(seq, title, thumbnail)
                events.add(dto)
            }
        }
        cursor.close()
        helper.close()
        return events
    }

    @SuppressLint("Range")
    fun getEventByseq(seq: Int?): Event {
        val helper = EventDBHelper(context)
        val db = helper.readableDatabase

        val selection = "${EventDBHelper.COL_SEQ} = ?"
        val selectionArgs = arrayOf(seq.toString())

        val cursor = db.query(
            EventDBHelper.TABLE_NAME, null, selection, selectionArgs,
            null, null, null
        )

        var event: Event? = null

        if (cursor.count > 0 && cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndex(EventDBHelper.COL_TITLE))
            val photo = cursor.getString(cursor.getColumnIndex(EventDBHelper.COL_PHOTO))
            event = Event(seq, title, photo)
        }

        cursor.close()
        db.close()

        // 만약 리뷰가 존재하지 않을 경우 빈 Review 객체 반환
        return Event(seq, null, null)
    }

    @SuppressLint("Range")
    fun getReviewByEventSeq(seq: Int?): Review {
        val helper = EventDBHelper(context)
        val db = helper.readableDatabase

        val selection = "${EventDBHelper.COL_RVSEQ} = ?"
        val selectionArgs = arrayOf(seq.toString())

        val cursor = db.query(
            EventDBHelper.TABLE_NAME, null, selection, selectionArgs,
            null, null, null
        )

        var review: Review? = null

        if (cursor.count > 0 && cursor.moveToFirst()) {
            val reviewSeq = cursor.getInt(cursor.getColumnIndex(EventDBHelper.COL_RVSEQ))
            val reviewText = cursor.getString(cursor.getColumnIndex(EventDBHelper.COL_RVTEXT))
            val reviewTitle = cursor.getString(cursor.getColumnIndex(EventDBHelper.COL_TITLE))
            val reviewImage = cursor.getString(cursor.getColumnIndex(EventDBHelper.COL_PHOTO))
            review = Review(reviewSeq, reviewText, reviewTitle, reviewImage)
        }

        cursor.close()
        db.close()

        // 만약 리뷰가 존재하지 않을 경우 빈 Review 객체 반환
        return review ?: Review(null, null, null, null)
    }

    @SuppressLint("Range")
    fun getAllReviews() : ArrayList<Review> {
        val helper = EventDBHelper(context)
        val db = helper.readableDatabase

        val cursor = db.query(
            EventDBHelper.TABLE_NAME, null,  "${EventDBHelper.COL_RVTEXT} IS NOT NULL", null,
            null, null, null
        )

        val reviews = arrayListOf<Review>()
        with(cursor) {
            while (moveToNext()) {
                val seq = getInt(getColumnIndex(EventDBHelper.COL_RVSEQ))
                val reviewText = getString(getColumnIndex(EventDBHelper.COL_RVTEXT))
                val reviewTitle = getString(getColumnIndex(EventDBHelper.COL_TITLE))
                val reviewImage = getString(getColumnIndex(EventDBHelper.COL_PHOTO))
                val dto = Review(seq, reviewText, reviewTitle, reviewImage)
                reviews.add(dto)
            }
        }
        cursor.close()
        helper.close()
        return reviews
    }

    fun insertEvent(event: Event) {
        val helper = EventDBHelper(context)
        val db = helper.writableDatabase

        val insertValue = ContentValues()

        if (!isSeqExists1(db, event.seq)) {

            insertValue.put(EventDBHelper.COL_SEQ, event.seq)
            insertValue.put(EventDBHelper.COL_TITLE, event.title)
            insertValue.put(EventDBHelper.COL_PHOTO, event.thumbnail)

            // Insert the new row, returning the primary key value of the new row
            val result = db.insert(EventDBHelper.TABLE_NAME, null, insertValue)

            val TAG = "hi"
            if (result == -1L) {
                // 삽입 실패
                Log.e(TAG, "Failed to insert event into the database.")
            } else {
                // 삽입 성공
                Log.d(TAG, "Event inserted with ID: $result")
            }
        }

        helper.close()
    }
    fun insertReview(review: Review) {

        Log.d("insertReview", "Trying to insert review: ${review.reviewSeq}")
        val helper = EventDBHelper(context)
        val db = helper.writableDatabase

        val insertValue = ContentValues()

        if (!isSeqExists2(db, review.reviewSeq)) {

            insertValue.put(EventDBHelper.COL_RVSEQ, review.reviewSeq)
            insertValue.put(EventDBHelper.COL_RVTEXT, review.reviewText)
            insertValue.put(EventDBHelper.COL_TITLE, review.reviewTitle)
            insertValue.put(EventDBHelper.COL_PHOTO, review.reviewImage)

            // Insert the new row, returning the primary key value of the new row
            val result = db.insert(EventDBHelper.TABLE_NAME, null, insertValue)

            val TAG = "hi"
            if (result == -1L) {
                // 삽입 실패
                Log.e(TAG, "Failed to insert event into the database.")
            } else {
                // 삽입 성공
                Log.d(TAG, "Event inserted with ID: $result")
            }
        }

        helper.close()
    }


    fun updateReview(review: Review) {
        val helper = EventDBHelper(context)
        val db = helper.writableDatabase

        val updateValue = ContentValues()
        updateValue.put(EventDBHelper.COL_RVTEXT, review.reviewText)

        val result = db.update(
            EventDBHelper.TABLE_NAME,
            updateValue,
            "${EventDBHelper.COL_RVSEQ} = ?",
            arrayOf(review.reviewSeq.toString())
        )

        val TAG = "hi"
        if (result == -1) {
            // 업데이트 실패
            Log.e(TAG, "Failed to update review in the database.")
        } else {
            // 업데이트 성공
            Log.d(TAG, "Review updated with ID: ${review.reviewSeq}")
        }
        helper.close()
    }

    fun deleteReview(review: Review) {
        val helper = EventDBHelper(context)
        val db = helper.writableDatabase

        val result = db.delete(
            TABLE_NAME,
            "${EventDBHelper.COL_RVSEQ} = ?",
            arrayOf(review.reviewSeq?.toString())
        )

        if (result > 0) {
            Log.d("EventDao", "Review deleted with ID: ${review.reviewSeq}")
        } else {
            Log.e("EventDao", "Failed to delete review with ID: ${review.reviewSeq}")
        }
        helper.close()
    }


    private fun isSeqExists1(db: SQLiteDatabase, seq: Int?): Boolean {
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_SEQ = $seq"
        val cursor = db.rawQuery(query, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    private fun isSeqExists2(db: SQLiteDatabase, seq: Int?): Boolean {
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_RVSEQ = $seq"
        val cursor = db.rawQuery(query, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}