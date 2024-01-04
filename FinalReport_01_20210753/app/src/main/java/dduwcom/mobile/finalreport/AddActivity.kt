package dduwcom.mobile.finalreport

import android.content.ContentValues
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dduwcom.mobile.finalreport.data.BookDBHelper
import dduwcom.mobile.finalreport.data.BookDto
import dduwcom.mobile.finalreport.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity(){
    val TAG = "AddActivity"

    lateinit var addBinding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addBinding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(addBinding.root)

        addBinding.ivAddPhoto.setImageResource(R.mipmap.book_image)

        /*추가버튼 클릭*/
        addBinding.btnAddBook.setOnClickListener {
            val photo = R.mipmap.book_image
            val title = addBinding.etAddTitle.text.toString()
            val author = addBinding.etAddAuthor.text.toString()
            val publisher = addBinding.etAddPublisher.text.toString()
            val price = addBinding.etAddPrice.text.toString()
            val newDto = BookDto(0, photo, title, author, publisher, price)      // 화면 값으로 dto 생성, id 는 임의의 값 0

            if (addFood(newDto) > 0) {
                setResult(RESULT_OK)
            } else {
                setResult(RESULT_CANCELED)
            }
            finish()
        }

        /*취소버튼 클릭*/
        addBinding.btnAddCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }


    /*추가할 정보를 담고 있는 dto 를 전달받아 DB에 추가, id 는 autoincrement 이므로 제외
    * DB추가 시 추가한 항목의 ID 값 반환, 추가 실패 시 -1 반환 */
    fun addFood(newDto: BookDto): Long {
        val helper = BookDBHelper(this)
        val db = helper.writableDatabase

        val newValues = ContentValues()
        newValues.put(BookDBHelper.COL_PHOTO, newDto.photo)
        newValues.put(BookDBHelper.COL_TITLE, newDto.title)
        newValues.put(BookDBHelper.COL_AUTHOR, newDto.author)
        newValues.put(BookDBHelper.COL_PUBLISHER, newDto.publisher)
        newValues.put(BookDBHelper.COL_PRICE, newDto.price)

        /*insert 한 항목의 id 를 반환*/
        val result = db.insert(BookDBHelper.TABLE_NAME, null, newValues)

        helper.close()      // DB 작업 후 close() 수행

        return result
    }
}



