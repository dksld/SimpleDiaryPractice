package com.dksld.simplediary

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dksld.simplediary.data.DiaryDao
import com.dksld.simplediary.data.DiaryDto
import com.dksld.simplediary.databinding.ActivityAddBinding
import java.util.Calendar

class AddActivity : AppCompatActivity() {
    val TAG = "AddActivityTag"
    lateinit var addBinding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        addBinding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(addBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 현재 날짜를 textView에 표시
        val calendar = Calendar.getInstance()
        var currentYear = calendar.get(Calendar.YEAR)
        var currentMonth = calendar.get(Calendar.MONTH)
        var currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        addBinding.tvDateAdd.text = "${currentYear}년 ${currentMonth+1}월 ${currentDay}일"

        // 날짜선택 버튼 클릭 시 DatePickerDialog 띄우기
        addBinding.btnDateAdd.setOnClickListener{
            val dialog = DatePickerDialog(this, object:DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    currentYear = year
                    currentMonth = month
                    currentDay = dayOfMonth

                    addBinding.tvDateAdd.text = "${currentYear}년 ${currentMonth+1}월 ${currentDay}일"
                }

            }, currentYear, currentMonth, currentDay)
            dialog.setCancelable(false)
            dialog.show()
        }

        // 기본 날씨이미지 띄우기
        addBinding.ivWeatherAdd.setImageResource(R.drawable.weather0)

        // 날씨선택 버튼 클릭 시 dialog 띄우기
        var selectedIndex = 0
        addBinding.btnWeatherAdd.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(this).apply {
                setTitle("날씨 선택")
                setSingleChoiceItems(R.array.weathers, selectedIndex){
                    _, index -> selectedIndex = index
                }
                setPositiveButton("선택 완료"){ _, _ ->
                    val image = when(selectedIndex){
                        1-> R.drawable.weather1
                        2->R.drawable.weather2
                        3->R.drawable.weather3
                        4->R.drawable.weather4
                        else->R.drawable.weather0
                    }

                    addBinding.ivWeatherAdd.setImageResource(image)

                }
                setCancelable(false)
            }
            builder.show()
        }

        // 추가버튼 클릭리스너
        addBinding.btnDiaryAdd.setOnClickListener{
            val title = addBinding.etTitleAdd.text.toString()
            val emotion = addBinding.etEmotionAdd.text.toString()
            val content = addBinding.etContentAdd.text.toString()

            if(title.length <= 0 || emotion.length <= 0 || content.length <= 0){
                Toast.makeText(this, "모든 정보를 입력하고 다시 추가하세요", Toast.LENGTH_SHORT).show()
            }else{
                val diaryDao = DiaryDao(this)
                var dto = DiaryDto(
                    null,
                    title,
                    currentYear,
                    currentMonth + 1,
                    currentDay,
                    selectedIndex,
                    emotion,
                    content
                )
                val addId = diaryDao.addDiary(dto)
                dto.id = addId

                if(addId > 0){
                    val resultIntent = Intent()
                    resultIntent.putExtra("addId", addId)
                    setResult(RESULT_OK, resultIntent)
                }else{
                    setResult(RESULT_CANCELED)
                }

                finish()
            }
        }

        // 취소버튼 클릭리스너
        addBinding.btnDiaryAddCancel.setOnClickListener{
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}