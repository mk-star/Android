package dduwcom.mobile.finalreport

import android.content.ContentValues
import android.os.Bundle
import android.provider.BaseColumns
import androidx.appcompat.app.AppCompatActivity
import dduwcom.mobile.finalreport.data.BookDBHelper
import dduwcom.mobile.finalreport.data.BookDao
import dduwcom.mobile.finalreport.data.BookDto
import dduwcom.mobile.finalreport.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    lateinit var updateBinding: ActivityUpdateBinding
    lateinit var bookDao: BookDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateBinding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(updateBinding.root)

        bookDao = BookDao(this)
        /*RecyclerView 에서 선택하여 전달한 dto 를 확인*/
        val dto = intent.getSerializableExtra("dto") as BookDto

        updateBinding.ivUpdatePhoto.setImageResource(dto.photo)
        updateBinding.etUpdateTitle.setText(dto.title)
        updateBinding.etUpdateAuthor.setText(dto.author)
        updateBinding.etUpdatePublisher.setText(dto.publisher)
        updateBinding.etUpdatePrcie.setText(dto.price)

        updateBinding.btnUpdateBook.setOnClickListener {
            /*dto 는 아래와 같이 기존의 dto 를 재사용할 수도 있음*/
            dto.title = updateBinding.etUpdateTitle.text.toString()
            dto.author = updateBinding.etUpdateAuthor.text.toString()
            dto.publisher = updateBinding.etUpdatePublisher.text.toString()
            dto.price = updateBinding.etUpdatePrcie.text.toString()

            if (bookDao.updateBook(dto) > 0) {
                setResult(RESULT_OK)      // update 를 수행했으므로 RESULT_OK 를 반환
            } else {
                setResult(RESULT_CANCELED)
            }
                finish()
        }

            updateBinding.btnUpdateCancel.setOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }
    }
}