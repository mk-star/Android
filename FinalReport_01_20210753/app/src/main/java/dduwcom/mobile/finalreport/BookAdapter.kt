package dduwcom.mobile.finalreport


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dduwcom.mobile.finalreport.data.BookDto
import dduwcom.mobile.finalreport.databinding.ListItemBinding

class BookAdapter (val books: ArrayList<BookDto>)
    : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    val TAG = "BookAdapter"
    // RecyclerView 에 표시할 전체 뷰의 개수 == 원본 데이터의 개수
    override fun getItemCount() = books.size

    // 각 항목의 뷰를 보관하는 Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(itemBinding, listener, lcListener)
    }

    /*재정의 필수 - 각 item view 의 항목에 데이터 결합 시 호출*/
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.itemBinding.ivPhoto.setImageResource(books[position].photo)
        holder.itemBinding.tvTitle.text = books[position].title
        holder.itemBinding.tvAuthor.text = books[position].author
        holder.itemBinding.tvPublisher.text = books[position].publisher
    }

    /*화면의 View 를 바인딩 형태로 보관하는 ViewHolder 클래스*/
    class BookViewHolder(val itemBinding: ListItemBinding, listener: OnItemClickListener?, lcListener: OnItemLongClickListener?)
        : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            /*list_item 의 root 항목(ConstraintLayout) 클릭 시*/
            itemBinding.root.setOnClickListener{
                listener?.onItemClick(it, adapterPosition)  // adapterPosition 은 클릭 위치 index
            }

            /*list_item 의 root 항목(ConstraintLayout) 롱클릭 시*/
            itemBinding.root.setOnLongClickListener{
                lcListener?.onItemLongClick(it, adapterPosition)
                true
            }

        }
    }
    /*사용자 정의 외부 롱클릭 이벤트 리스너 정의 부분*/
    interface OnItemLongClickListener {
        fun onItemLongClick(view: View, position: Int)
    }

    var lcListener : OnItemLongClickListener? = null

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        this.lcListener = listener
    }


    /*사용자 정의 외부 클릭 이벤트 리스너 정의 부분*/
    interface OnItemClickListener {
        fun onItemClick(view : View, position : Int)
    }

    var listener : OnItemClickListener? = null  // listener 를 사용하지 않을 때도 있으므로 null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }
}