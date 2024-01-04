package com.example.myapplication.ui

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.Event
import com.example.myapplication.data.Review
import com.example.myapplication.databinding.ListItemBinding
import com.example.myapplication.databinding.ReviewItemBinding

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewHolder>() {
    var reviews: List<Review>? = null

    override fun getItemCount(): Int {
        return reviews?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val itemBinding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewHolder(itemBinding, listener, deleteClickListener)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.itemBinding.tvTitle.text = Html.fromHtml(reviews?.get(position)?.reviewTitle)
        holder.itemBinding.tvReview.text = Html.fromHtml(reviews?.get(position)?.reviewText)
        val currentReview = reviews?.get(position)
        Glide.with(holder.itemView.context)
            .load(currentReview?.reviewImage) // 이미지 URL이 있는 경우
            //.placeholder(R.drawable.placeholder_image) // 로딩 중에 보여줄 이미지
            //.error(R.drawable.error_image) // 이미지 로딩 실패 시 보여줄 이미지
            .into(holder.itemBinding.imgReview)
    }

    interface OnItemClickListener {
        fun onItemClick(view : View, position : Int)
    }
    interface OnItemDeleteClickListener {
        fun onItemDeleteClick(position: Int)
    }

    var listener : OnItemClickListener? = null  // listener 를 사용하지 않을 때도 있으므로 null
    var deleteClickListener: OnItemDeleteClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }
    fun setOnItemDeleteClickListener(listener: OnItemDeleteClickListener?) {
        this.deleteClickListener = listener
    }
    fun removeItem(position: Int) {
        reviews?.toMutableList()?.run {
            removeAt(position)
            reviews = this
            notifyItemRemoved(position)
        }
    }

    class ReviewHolder(val itemBinding: ReviewItemBinding, listener: OnItemClickListener?, deleteClickListener: OnItemDeleteClickListener?) : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            /*list_item 의 root 항목(ConstraintLayout) 클릭 시*/
            itemBinding.root.setOnClickListener {
                listener?.onItemClick(it, adapterPosition)  // adapterPosition 은 클릭 위치 index
            }
            /*삭제 버튼 클릭 시*/
            itemBinding.btnDelete.setOnClickListener {
                deleteClickListener?.onItemDeleteClick(adapterPosition)
            }
        }
    }
}