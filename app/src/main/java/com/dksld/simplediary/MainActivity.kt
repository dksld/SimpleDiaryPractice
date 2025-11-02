package com.dksld.simplediary

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dksld.simplediary.data.DiaryDao
import com.dksld.simplediary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivityTag"
    lateinit var binding: ActivityMainBinding
    lateinit var diaryDao: DiaryDao
    lateinit var adapter: DiaryAdapter

    val ADDACTIVITY_CODE = 100
    val UPDATEACTIVITY_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        diaryDao = DiaryDao(this)
        var allDiaries = diaryDao.getAllDiaries()
        adapter = DiaryAdapter(allDiaries)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        // 항목 롱클릭시 삭제 dialog 띄우기
        adapter.setOnDiaryLongClickListener(object:DiaryAdapter.OnDiaryLongClickListener{
            override fun onDiaryLongClick(view: View, pos: Int) {
                val id = adapter.diaryList[pos].id
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity).apply{
                    setTitle("다이어리 삭제")
                    setMessage("${adapter.diaryList[pos].title}을(를)\n삭제하시겠습니까?")
                    setPositiveButton("삭제"){ di, _ ->
                        val recordCount = diaryDao.removeDiary(id)
                        if(recordCount > 0){
                            Log.d(TAG, "삭제된 레코드 개수: $recordCount")
                            adapter.diaryList = diaryDao.getAllDiaries()
                            adapter.notifyDataSetChanged()
                        }
                    }
                    setNegativeButton("취소", null)
                    setCancelable(false)
                }
                builder.show()
            }
        })

        // 항목 클릭시 UpdateActivity 띄우기
        adapter.setOnDiaryClickListener(object:DiaryAdapter.OnDiaryClickListener{
            override fun onDiaryClick(view: View, pos: Int) {
                Log.i(TAG, "클릭한 항목 title: ${adapter.diaryList[pos].title}")

                val intent = Intent(this@MainActivity, UpdateActivity::class.java)
                intent.putExtra("listIndex", pos)
                intent.putExtra("item", adapter.diaryList[pos])

                startActivityForResult(intent, UPDATEACTIVITY_CODE)
            }
        })
    }

    // 옵션메뉴 inflate
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.diary_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.addDiaryItem){   // 메뉴에서 추가 클릭시 AddActivity 띄우기
            val intent: Intent = Intent(this, AddActivity::class.java)
            startActivityForResult(intent, ADDACTIVITY_CODE)
        }else if(item.itemId == R.id.searchItem){   // 메뉴에서 다이어리 검색 클릭시 SearchActivity 띄우기
            val intent: Intent = Intent(this, searchActivity::class.java)
            startActivity(intent)
        }else if(item.itemId == R.id.introduceItem){    // 메뉴에서 앱 소개 클릭시 dialog 띄우기
            val builder = AlertDialog.Builder(this).apply{
                setTitle("앱 소개")
                setMessage("항목 추가: 우측 상단 점 3개 > 추가" +
                        "\n수정: 항목 클릭 후 수정" +
                        "\n삭제: 항목 롱클릭 후 확인" +
                        "\n제목 검색: 우측 상단 점 3개 > 다이어리 검색" +
                        "\n제목 검색의 경우 해당 키워드가 포함된 제목들을 조회만 할 수 있는 기능입니다." +
                        "\n날씨 이미지들은 직접 그려서 사용하였습니다." +
                        "\n앱을 잘 사용해주시면 좋겠습니다.")
                setPositiveButton("확인", null)
                setCancelable(false)
            }
            builder.show()
        }else if(item.itemId == R.id.finishItem){   // 메뉴에서 종료 클릭시 dialog 띄우기
            val builder = AlertDialog.Builder(this).apply {
                setTitle("앱 종료")
                setMessage("앱을 종료하시겠습니까?")
                setPositiveButton("종료"){
                    _, _ -> finish()
                }
                setNegativeButton("취소", null)
                setCancelable(false)
            }
            builder.show()
        }
        return true
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == ADDACTIVITY_CODE){    // AddActivity가 종료되었을 때
            when(resultCode){
                RESULT_OK ->{
                    val resultId = data?.getLongExtra("addId", -1)

                    Log.d(TAG, "추가된 항목 id: $resultId")

                    // 리사이클러 뷰 갱신
                    adapter.diaryList = diaryDao.getAllDiaries()
                    adapter.notifyDataSetChanged()
                }
            }
        }else if(requestCode == UPDATEACTIVITY_CODE){   // UpdateActivity가 종료되었을 때
            when(resultCode){
                RESULT_OK -> {
                    val recordCount = data?.getIntExtra("recordCount", -1)

                    Log.d(TAG, "수정된 레코드 개수: $recordCount")

                    // 리사이클러 뷰 갱신
                    adapter.diaryList = diaryDao.getAllDiaries()
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}