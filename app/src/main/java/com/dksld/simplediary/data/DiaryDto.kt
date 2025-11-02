package com.dksld.simplediary.data

import java.io.Serializable

data class DiaryDto(var id: Long?, var title: String, var year:Int, var month:Int, var day:Int, var weatherImage: Int, var emotion: String, var content: String):Serializable{
    override fun toString(): String {
        return "제목: $title, 날짜: ${year}년 ${month}월 ${day}일, 기분: $emotion"
    }
}
