package dduwcom.mobile.finalreport
//과제명: 도서 관리 앱
//분반: 01 분반
//학번: 20210753 성명: 김민경
//제출일: 2023년 6월
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import dduwcom.mobile.finalreport.data.BookDBHelper
import dduwcom.mobile.finalreport.data.BookDao
import dduwcom.mobile.finalreport.data.BookDto
import dduwcom.mobile.finalreport.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val REQ_ADD = 100
    val REQ_UPDATE = 200

    lateinit var binding : ActivityMainBinding
    lateinit var adapter : BookAdapter
    lateinit var books : ArrayList<BookDto>
    lateinit var bookDao: BookDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvBooks.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        bookDao = BookDao(this)
        books = bookDao.getAllBooks()
        adapter = BookAdapter(books)
        binding.rvBooks.adapter = adapter

        val onClickListener = object : BookAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                /*클릭 항목의 dto 를 intent에 저장 후 UpdateActivity 실행*/
               val intent = Intent(this@MainActivity, UpdateActivity::class.java)
                intent.putExtra("dto", books.get(position))
                startActivityForResult(intent, REQ_UPDATE)
            }
        }

        adapter.setOnItemClickListener(onClickListener)

        /*RecyclerView 항목 롱클릭 시 실행할 객체*/
        val onLongClickListener = object: BookAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int) {
                AlertDialog.Builder(this@MainActivity).run {
                    setTitle("책 삭제")
                    setMessage(books.get(position).title + " 을 삭제하시겠습니까?")
                    setCancelable(false)
                    setPositiveButton("삭제", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            if (bookDao.deleteBook(books.get(position).id) > 0) {
                                refreshList(RESULT_OK)
                            }
                        }
                    })
                    setNegativeButton("취소", null)
                    show()
                }
            }
        }

        adapter.setOnItemLongClickListener(onLongClickListener)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuBook -> {
                val intent = Intent(this, AddActivity::class.java)
                startActivityForResult(intent, REQ_ADD)
            }
            R.id.introduce -> {
                val intent = Intent(this, IntroduceActivity::class.java)
                startActivity(intent)
            }
            R.id.quit -> {
                AlertDialog.Builder(this).run {
                    setTitle("앱 종료")
                    setMessage("앱을 종료하시겠습니까?")
                    setCancelable(false)
                    setPositiveButton("종료", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            finish()
                        }
                    })
                    setNegativeButton("취소", null)
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_UPDATE -> {
                refreshList(resultCode)
            }
            REQ_ADD -> {
                refreshList(resultCode)
            }
        }
    }
   private fun refreshList(resultCode: Int) {
        if (resultCode == RESULT_OK) {
            books.clear()
            books.addAll(bookDao.getAllBooks())
            adapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this@MainActivity, "취소됨", Toast.LENGTH_SHORT).show()
        }
    }
}


