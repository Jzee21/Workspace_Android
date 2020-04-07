package com.multi.androidlectureexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/*
*   Content Provider (내용제공자)
*   App 에서 관리하는 데이터(SQLite DB)를 다른 App이 접근할 수 있도록 하는 기능
*   CP 는 Android 구성요소 중 하나로 안드로이드 시스템에 의해 관리된다.
*
*   AndroidManifest.xml 파일에 등록하여 사용
*
*   CP가 필요한 이유는 보안규정 때문
*   Android App 은 오직 자신의 App 안에서 생성한 데이터만 사용할 수 있다.
*   다른 App 이 가진 데이터의 접근권한은 없다
*
*   CP를 이용하면 다른 App 의 데이터에 접근할 수 있다.
*   (일반적으로 Database 에 접근하는 방식을 이용)
*   그 이유는 CP 가 CRUD 를 기반으로 하고있기 때문이다.
*
*   1. 데이터베이스(SQLite)를 이용하기 때문에 SQLiteOpenHelper class 를 이용한다
*
*
* */
public class Example23_CPExamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example23_cp_exam);

        // _23_empNameEt
        // _23_empAgeEt
        // _23_empMobileEt

        // _23_empInsertBtn
        Button _23_empInsertBtn = findViewById(R.id._23_empInsertBtn);
        _23_empInsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 원래는 Activity에서 데이터를 가져오겠지만... ㅎ

                // CP 탐색
                String uriString = "content://com.multi.person.provider/person";
                Uri uri = new Uri.Builder().build().parse(uriString);

                // HashMap 형태로 DB 에 입력할 데이터를 저장
                ContentValues values = new ContentValues();
                values.put("name", "홍길동");
                values.put("age", 20);
                values.put("mobile", "010-1111-5555");

                getContentResolver().insert(uri, values);
                Log.i("DBTest", "Insert Data");
            }
        });

        //
        // _23_empSelectBtn
        // _23_resultTv
    }
}

class PersonDatabaseHelper extends SQLiteOpenHelper {

    public PersonDatabaseHelper(@Nullable Context context) {
        super(context, "person.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // DB 생성 시점에 Table도 같이 생성
        String sql = "CREATE TABLE IF NOT EXISTS "
                + "person(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, age INTEGER, mobile TEXT)";
        db.execSQL(sql);
        Log.i("DBTest", "onCreate()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}