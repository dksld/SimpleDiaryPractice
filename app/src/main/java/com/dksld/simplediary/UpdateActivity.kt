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
import com.dksld.simplediary.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {
    val TAG = "UpdateActivityTag"
    lateinit var updateBinding: ActivityUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        updateBinding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(updateBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var dto = intent.getSerializableExtra("item") as DiaryDto

        // 가져온 정보 표시
        updateBinding.etTitleUpdate.setText(dto.title)
        updateBinding.etEmotionUpdate.setText((dto.emotion))
        updateBinding.etContentUpdate.setText(dto.content)

        var currentYear = dto.year
        var currentMonth = dto.month-1
        var currentDay = dto.day

        updateBinding.tvDateUpdate.setText("${currentYear}년 ${currentMonth+1}월 ${currentDay}일")

        var selectedIndex = dto.weatherImage

        var image = when(selectedIndex){
            1->R.drawable.weather1
            2->R.drawable.weather2
            3->R.drawable.weather3
            4->R.drawable.weather4
            else->R.drawable.weather0

        }
        updateBinding.ivWeatherUpdate.setImageResource(image)

        // 날짜선택 클릭시 DatePickerDialog 띄우기
        updateBinding.btnDateUpdate.setOnClickListener{
            val dialog = DatePickerDialog(this, object: DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    currentYear = year
                    currentMonth = month
                    currentDay = dayOfMonth

                    updateBinding.tvDateUpdate.text = "${currentYear}년 ${currentMonth+1}월 ${currentDay}일"
                }
            }, currentYear, currentMonth, currentDay)
            dialog.setCancelable(false)
            dialog.show()
        }

        // 날씨선택 클릭시 dialog 띄우기
        updateBinding.btnWeatherUpdate.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(this).apply {
                setTitle("날씨 선택")
                setSingleChoiceItems(R.array.weathers, selectedIndex){
                        _, index -> selectedIndex = index
                }
                setPositiveButton("선택 완료"){ _, _ ->
                    val image = when(selectedIndex){
                        1->R.drawable.weather1
                        2->R.drawable.weather2
                        3->R.drawable.weather3
                        4->R.drawable.weather4
                        else->R.drawable.weather0
                    }

                    updateBinding.ivWeatherUpdate.setImageResource(image)

                }
                setCancelable(false)
            }
            builder.show()
        }

        // 수정버튼 클릭리스너
        updateBinding.btnDiaryUpdate.setOnClickListener{
            val title = updateBinding.etTitleUpdate.text.toString()
            val emotion = updateBinding.etEmotionUpdate.text.toString()
            val content = updateBinding.etContentUpdate.text.toString()

            if(title.length <= 0 || emotion.length <= 0 || content.length <= 0){
                Toast.makeText(this, "모든 정보를 입력하고 다시 수정하세요", Toast.LENGTH_SHORT).show()
            }else{
                val diaryDao = DiaryDao(this)

                var newDto = DiaryDto(
                    dto.id,
                    title,
                    currentYear,
                    currentMonth + 1,
                    currentDay,
                    selectedIndex,
                    emotion,
                    content
                )
                val recordCount = diaryDao.modifyDiary(newDto)

                if(recordCount > 0){
                    val resultIntent = Intent()
                    resultIntent.putExtra("recordCount", recordCount)

                    setResult(RESULT_OK, resultIntent)
                }else{
                    setResult(RESULT_CANCELED)
                }

                finish()
            }
        }

        // 취소버튼 클릭리스너
        updateBinding.btnDiaryUpdateCancel.setOnClickListener{
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}