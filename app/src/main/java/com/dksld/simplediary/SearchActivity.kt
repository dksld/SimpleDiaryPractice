package com.dksld.simplediary

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dksld.simplediary.data.DiaryDao
import com.dksld.simplediary.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    val TAG = "SearchActivityTag"
    lateinit var searchBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        searchBinding.tvSearchResult.text = ""

        // 검색버튼 클릭시 DiaryDao의 함수를 이용하여 정보를 가져오고 출력
        searchBinding.btnSearch.setOnClickListener{
            val keyword = searchBinding.etSearch.text.toString()
            val titleList = DiaryDao(this).getDiaryTitlesByKeyword(keyword)

            if(titleList.size > 0){
                val titleStr = StringBuilder()
                titleStr.append("검색결과\n")

                for(i in 0 until titleList.size){
                    titleStr.append("\n${titleList[i]}")
                }

                searchBinding.tvSearchResult.text = titleStr.toString()
            }else{
                searchBinding.tvSearchResult.text = "'${keyword}'에 대한 검색결과가 없습니다."
            }
        }

        // 뒤로가기 클릭리스너
        searchBinding.btnBack.setOnClickListener{
            finish()
        }
    }
}