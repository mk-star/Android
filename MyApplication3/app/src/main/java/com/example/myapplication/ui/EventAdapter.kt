package com.example.myapplication.ui

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.Event
import com.example.myapplication.databinding.ListItemBinding

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventHolder>() {
    var events: List<Event>? = null

    override fun getItemCount(): Int {
        return events?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventHolder(itemBinding, listener)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.itemBinding.evnTitle.text = Html.fromHtml(events?.get(position)?.title)
        val currentEvent = events?.get(position)
        Glide.with(holder.itemView.context)
            .load(currentEvent?.thumbnail) // 이미지 URL이 있는 경우
            //.placeholder(R.drawable.placeholder_image) // 로딩 중에 보여줄 이미지
            //.error(R.drawable.error_image) // 이미지 로딩 실패 시 보여줄 이미지
            .into(holder.itemBinding.evnImage)
    }

    interface OnItemClickListener {
        fun onItemClick(view : View, position : Int)
    }
    var listener : OnItemClickListener? = null  // listener 를 사용하지 않을 때도 있으므로 null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    class EventHolder(val itemBinding: ListItemBinding, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            /*list_item 의 root 항목(ConstraintLayout) 클릭 시*/
            itemBinding.root.setOnClickListener {
                listener?.onItemClick(it, adapterPosition)  // adapterPosition 은 클릭 위치 index
            }
        }
    }
}