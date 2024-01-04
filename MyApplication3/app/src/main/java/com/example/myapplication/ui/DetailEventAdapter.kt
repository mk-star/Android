package com.example.myapplication.ui
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.DetailEvent

class DetailEventAdapter(private val context: Context, private val detailEventList: List<DetailEvent>) :
    RecyclerView.Adapter<DetailEventAdapter.DetailEventViewHolder>() {

    class DetailEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.detailTitle)
        val thumbnail: ImageView = itemView.findViewById(R.id.detailImage)
        val startDate: TextView = itemView.findViewById(R.id.detailStartDate)
        val endDate: TextView = itemView.findViewById(R.id.detailEndDate)
        val place: TextView = itemView.findViewById(R.id.detailPlace)
        val price: TextView = itemView.findViewById(R.id.detailPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailEventViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.detail_item, parent, false)
        return DetailEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailEventViewHolder, position: Int) {
        val currentDetailEvent = detailEventList[position]

        holder.title.text = currentDetailEvent.title
        holder.startDate.text = "시작일: ${currentDetailEvent.startDate}"
        holder.endDate.text = "종료일: ${currentDetailEvent.endDate}"
        holder.place.text = "장소: ${currentDetailEvent.place}"
        holder.price.text = "가격: ${currentDetailEvent.price}"

        // 여기에서 이미지 로딩 등의 작업을 처리할 수 있습니다.
        // Glide 등의 라이브러리를 사용하면 편리합니다.
        Glide.with(context)
            .load(currentDetailEvent.imgUrl)
            .into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return detailEventList.size
    }
}
