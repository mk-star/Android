package dduwcom.mobile.finalreport.data

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import dduwcom.mobile.finalreport.R

class BookDao(val context: Context) {
    fun getAllBooks() : ArrayList<BookDto> {
        val helper = BookDBHelper(context)
        val db = helper.readableDatabase

        val cursor = db.query(
            BookDBHelper.TABLE_NAME, null, null, null,
            null, null, null
        )


        val books = arrayListOf<BookDto>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndex(BaseColumns._ID))
                val photo = getInt(getColumnIndex(BookDBHelper.COL_PHOTO))
                val title = getString(getColumnIndex(BookDBHelper.COL_TITLE))
                val author = getString(getColumnIndex(BookDBHelper.COL_AUTHOR))
                val publisher = getString(getColumnIndex(BookDBHelper.COL_PUBLISHER))
                val price = getString(getColumnIndex(BookDBHelper.COL_PRICE))
                val dto = BookDto(id, photo, title, author, publisher, price)
                books.add(dto)
            }
        }
        cursor.close()
        helper.close()
        return books
    }

    /*update 정보를 담고 있는 dto 를 전달 받아 dto 의 id 를 기준으로 수정*/
    fun updateBook(dto: BookDto): Int {
        val helper = BookDBHelper(context)
        val db = helper.writableDatabase
        val updateValue = ContentValues()

        updateValue.put(BookDBHelper.COL_TITLE, dto.title)
        updateValue.put(BookDBHelper.COL_AUTHOR, dto.author)
        updateValue.put(BookDBHelper.COL_PUBLISHER, dto.publisher)
        updateValue.put(BookDBHelper.COL_PRICE, dto.price)
        val whereClause = "${BaseColumns._ID}=?"
        val whereArgs = arrayOf(dto.id.toString())

        /*upate 가 적용된 레코드의 개수 반환*/
        val result = db.update(
            BookDBHelper.TABLE_NAME, updateValue, whereClause, whereArgs
        )

        helper.close()      // DB 작업 후에는 close()

        return result
    }

    fun deleteBook(id: Long) : Int {
        val helper = BookDBHelper(context)
        val db = helper.writableDatabase

        val whereClause = "${BaseColumns._ID}=?"
        val whereArgs = arrayOf(id.toString())

        val result = db.delete(BookDBHelper.TABLE_NAME, whereClause, whereArgs)

        helper.close()
        return result
    }
}
