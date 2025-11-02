package com.dksld.simplediary.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

class DiaryDBHelper(context: Context?): SQLiteOpenHelper(context, DB_NAME, null, 1) {
    val TAG = "DiaryDBHelper"

    companion object {
        const val DB_NAME = "diary_db"
        const val TABLE_NAME = "diary_table"
        const val COL_TITLE = "title"
        const val COL_YEAR = "year"
        const val COL_MONTH = "month"
        const val COL_DAY = "day"
        const val COL_WEATHER = "weather"
        const val COL_EMOTION = "emotion"
        const val COL_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME ( ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COL_TITLE TEXT, $COL_YEAR INTEGER, $COL_MONTH INTEGER, $COL_DAY INTEGER, $COL_WEATHER INTEGER, $COL_EMOTION TEXT, $COL_CONTENT TEXT )"
        Log.d(TAG, CREATE_TABLE)
        db?.execSQL(CREATE_TABLE)

        insertSample(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun insertSample(db: SQLiteDatabase?){  // 샘플데이터 추가함수
        val sampleList = ArrayList<DiaryDto>()

        sampleList.add(DiaryDto(null, "개강", 2025, 3, 4, 2, "긴장함", "오랜만에 학교갔더니 피곤했다. 일찍 자야겠다."))
        sampleList.add(DiaryDto(null, "개강총회", 2025, 3, 11, 1, "속상함", "개강총회 갔다온거 너무너무 후회된다. 나만 못 어울리는 것 같았기 때문이다."))
        sampleList.add(DiaryDto(null, "만우절", 2025, 4, 1, 3, "외로움", "개강총회 이후로 동기들이랑 멀어진 것 같아서 너무 외롭다. 다 거짓말이었으면 좋겠다."))
        sampleList.add(DiaryDto(null, "최악의 생일", 2025, 5, 20, 0, "화남", "생일인데 기분 상한 일들만 겪어서 마음에 안 들었다. 다시는 겪고 싶지 않은 생일이었다."))
        sampleList.add(DiaryDto(null, "시험끝!", 2025, 6, 17, 0, "행복함", "제일 길게 느껴졌던 한 학기였는데 드디어 시험이 끝나서 행복하다."))

        for(i in 0 until sampleList.size){
            var newRow = ContentValues()
            newRow.put(COL_TITLE, sampleList[i].title)
            newRow.put(COL_YEAR, sampleList[i].year)
            newRow.put(COL_MONTH, sampleList[i].month)
            newRow.put(COL_DAY, sampleList[i].day)
            newRow.put(COL_WEATHER, sampleList[i].weatherImage)
            newRow.put(COL_EMOTION, sampleList[i].emotion)
            newRow.put(COL_CONTENT, sampleList[i].content)
            db?.insert(DiaryDBHelper.TABLE_NAME, null, newRow)
        }
    }
}