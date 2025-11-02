package com.dksld.simplediary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dksld.simplediary.data.DiaryDto
import com.dksld.simplediary.databinding.ListItemBinding
import com.dksld.simplediary.R

class DiaryAdapter(var diaryList: MutableList<DiaryDto>): RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {
    override fun getItemCount(): Int = diaryList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        // 날짜 연결
        val dateStr = "${diaryList[position].year}년 ${diaryList[position].month}월 ${diaryList[position].day}일"
        holder.itemBinding.tvDate.text = dateStr

        // 제목, 기분 연결
        holder.itemBinding.tvTitle.text = diaryList[position].title
        holder.itemBinding.tvEmotion.text = diaryList[position].emotion

        // 날씨 이미지 연결
        val image = when(diaryList[position].weatherImage){
            1-> R.drawable.weather1
            2-> R.drawable.weather2
            3-> R.drawable.weather3
            4-> R.drawable.weather4
            else->R.drawable.weather0
        }

        holder.itemBinding.ivWeather.setImageResource(image)
    }

    inner class DiaryViewHolder(val itemBinding:ListItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        init{
            itemBinding.root.setOnClickListener{
                diaryClickListener.onDiaryClick(it, adapterPosition)
            }

            itemBinding.root.setOnLongClickListener{
                diaryLongClickListener.onDiaryLongClick(it, adapterPosition)
                true
            }
        }
    }

    // 클릭리스너
    interface OnDiaryClickListener{
        fun onDiaryClick(view: View, pos: Int):Unit
    }

    lateinit var diaryClickListener:OnDiaryClickListener

    fun setOnDiaryClickListener(listener: OnDiaryClickListener){
        this.diaryClickListener = listener
    }

    // 롱클릭리스너
    interface OnDiaryLongClickListener{
        fun onDiaryLongClick(view: View, pos: Int):Unit
    }

    lateinit var diaryLongClickListener:OnDiaryLongClickListener

    fun setOnDiaryLongClickListener(listener: OnDiaryLongClickListener){
        this.diaryLongClickListener = listener
    }
}