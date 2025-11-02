package com.dksld.simplediary.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns

class DiaryDao(context: Context) {
    val helper: DiaryDBHelper

    init{
        helper = DiaryDBHelper(context)
    }

    @SuppressLint("Range")
    fun getAllDiaries() : MutableList<DiaryDto>{    // DB에 저장된 모든 정보를 가져온 리스트 반환
        val db = helper.readableDatabase
        val cursor = db.query(DiaryDBHelper.TABLE_NAME, null, null, null, null, null, null)

        val list = arrayListOf<DiaryDto>()
        with(cursor){
            while(moveToNext()){
                val id = getLong(getColumnIndex(BaseColumns._ID))
                val title = getString(getColumnIndex(DiaryDBHelper.COL_TITLE))
                val year = getInt(getColumnIndex(DiaryDBHelper.COL_YEAR))
                val month = getInt(getColumnIndex(DiaryDBHelper.COL_MONTH))
                val day = getInt(getColumnIndex(DiaryDBHelper.COL_DAY))
                val weather = getInt(getColumnIndex(DiaryDBHelper.COL_WEATHER))
                val emotion = getString(getColumnIndex(DiaryDBHelper.COL_EMOTION))
                val content = getString(getColumnIndex(DiaryDBHelper.COL_CONTENT))

                val dto = DiaryDto(id, title, year, month, day, weather, emotion, content)
                list.add(dto)
            }
        }

        cursor.close()
        helper.close()
        return list
    }

    fun addDiary(diaryDto:DiaryDto): Long{  // 새로운 항목 추가
        val db = helper.writableDatabase
        val newRow = ContentValues()
        newRow.put(DiaryDBHelper.COL_TITLE, diaryDto.title)
        newRow.put(DiaryDBHelper.COL_YEAR, diaryDto.year)
        newRow.put(DiaryDBHelper.COL_MONTH, diaryDto.month)
        newRow.put(DiaryDBHelper.COL_DAY, diaryDto.day)
        newRow.put(DiaryDBHelper.COL_WEATHER, diaryDto.weatherImage)
        newRow.put(DiaryDBHelper.COL_EMOTION, diaryDto.emotion)
        newRow.put(DiaryDBHelper.COL_CONTENT, diaryDto.content)

        val ret = db.insert(DiaryDBHelper.TABLE_NAME, null, newRow)

        helper.close()
        return ret  // 추가된 항목의 id 반환
    }

    fun modifyDiary(diaryDto: DiaryDto):Int{    // 기존 항목 수정
        val db = helper.writableDatabase

        val updateRow = ContentValues()
        updateRow.put(DiaryDBHelper.COL_TITLE, diaryDto.title)
        updateRow.put(DiaryDBHelper.COL_YEAR, diaryDto.year)
        updateRow.put(DiaryDBHelper.COL_MONTH, diaryDto.month)
        updateRow.put(DiaryDBHelper.COL_DAY, diaryDto.day)
        updateRow.put(DiaryDBHelper.COL_WEATHER, diaryDto.weatherImage)
        updateRow.put(DiaryDBHelper.COL_EMOTION, diaryDto.emotion)
        updateRow.put(DiaryDBHelper.COL_CONTENT, diaryDto.content)

        val whereClause = "${BaseColumns._ID}=?"
        val whereArgs = arrayOf(diaryDto.id.toString())

        val ret = db.update(DiaryDBHelper.TABLE_NAME, updateRow, whereClause, whereArgs)
        helper.close()

        return ret  // 변경된 레코드 수 반환
    }

    fun removeDiary(id: Long?): Int{    // 기존 항목 삭제
        if(id == null){
            return -1
        }

        val db = helper.writableDatabase

        val whereClause = "${BaseColumns._ID}=?"
        val whereArgs = arrayOf(id.toString())

        val ret = db.delete(DiaryDBHelper.TABLE_NAME, whereClause, whereArgs)
        helper.close()
        return ret  // 삭제된 레코드 수 반환
    }

    fun getDiaryTitlesByKeyword(keyword: String): List<String>{ // 키워드가 포함된 title List를 반환
        val db = helper.readableDatabase

        val sql = "SELECT ${DiaryDBHelper.COL_TITLE} FROM ${DiaryDBHelper.TABLE_NAME} WHERE ${DiaryDBHelper.COL_TITLE} LIKE '%${keyword}%'"
        val cursor = db.rawQuery(sql, null)

        val list = arrayListOf<String>()
        with(cursor){
            while(moveToNext()){
                list.add(getString(getColumnIndexOrThrow(DiaryDBHelper.COL_TITLE)))
            }
        }

        return list
    }

}