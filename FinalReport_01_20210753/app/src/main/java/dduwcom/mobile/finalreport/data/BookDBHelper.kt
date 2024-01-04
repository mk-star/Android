package dduwcom.mobile.finalreport.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import dduwcom.mobile.finalreport.R

class BookDBHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1){
    val TAG = "BookDBHelper"
    companion object {
        const val DB_NAME = "book_db"
        const val TABLE_NAME = "book_table"
        const val COL_PHOTO = "book_photo"
        const val COL_TITLE = "book_title"
        const val  COL_AUTHOR = "book_author"
        const val COL_PUBLISHER = "book_publisher"
        const val COL_PRICE = "book_price"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME ( ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COL_PHOTO INT, $COL_TITLE TEXT, $COL_AUTHOR TEXT, $COL_PUBLISHER TEXT, $COL_PRICE TEXT)"
        Log.d(TAG, CREATE_TABLE)
        db?.execSQL(CREATE_TABLE)

        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, ${R.mipmap.book_image1}, '잠옷을 입으렴', '이도우', '위즈덤하우스', '13,500')")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, ${R.mipmap.book_image2}, '지구에서 한아뿐', '정세랑', '난다', '11,700')")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, ${R.mipmap.book_image3}, '모순', '양귀자', '쓰다', '11,700')")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, ${R.mipmap.book_image4}, '칵테일, 러브, 좀비', '조예은', '안전가옥', '9,000')")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, ${R.mipmap.book_image5}, '시선으로부터', '정세랑', '문학동네', '12,600')")

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE ="DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }
}