1. 앱 이름: Simple Diary
2. 앱 소개: 안드로이드 스튜디오 UI 기초를 학습한 것을 바탕으로 간단한 다이어리 앱을 구현해 보았습니다.

3. 기능
- 샘플 데이터 5개 제공
- 다이어리 항목 추가
- 기존 항목 수정
- 기존 항목 삭제
- 특정 키워드가 포함된 다이어리 제목 검색

4. 사용 기술
- 날짜 선택 시 DatePickerDialog 이용
- 날씨 선택 시 Dialog에서 선택한 것에 따른 이미지 변경
- 다이어리 항목은 RecyclerView로 제시
- 데이터 저장은 SQLite 사용

5. 개선점
- 검색 기능 개선: 특정 키워드가 다이어리 내용에 포함된 데이터도 찾아낼 수 있도록 개선, 검색 결과로 나오는 데이터들도 RecyclerView로 제시하도록 개선
- DB 개선: SQLite 대신 ROOM DB 사용해보기

### MainActivity
<img width="380" height="775" alt="Diary Main" src="https://github.com/user-attachments/assets/d9d63fcc-6954-4955-9245-6f8a1adaebb2" />

### Menu
<img width="379" height="214" alt="Diary Menu" src="https://github.com/user-attachments/assets/ad4c8f15-09e4-4553-9143-116375008790" />

### AddActivity
<img width="378" height="771" alt="Diary Add" src="https://github.com/user-attachments/assets/38bed097-0c75-4a3b-b89f-bb1e8f56649c" />

### UpdateActivity
<img width="374" height="770" alt="Diary Update" src="https://github.com/user-attachments/assets/b2595e7a-2f33-4649-8762-bd21847ccd0a" />

### 삭제 확인
<img width="369" height="767" alt="Diary Delete" src="https://github.com/user-attachments/assets/db21b8ba-8021-4274-a561-904268b522c5" />

### 제목 검색
<img width="380" height="768" alt="Diary Search" src="https://github.com/user-attachments/assets/b4206aae-5ecd-4b63-8219-677de305bc78" />
