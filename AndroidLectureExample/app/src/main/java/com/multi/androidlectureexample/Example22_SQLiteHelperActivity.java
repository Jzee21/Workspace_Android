package com.multi.androidlectureexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// SQLiteOpenHelper 를 이용하기 위해서는 별도의 Class를 Define 해주어야 한다.
class MyDatabaseHelper extends SQLiteOpenHelper {

//    public static final int DATABASE_VERSION = 1;
//    public static final String DATABASE_NAME = "";

    /* SQLiteOpenHelper 클래스는 Default 생성자가 없다. */
    public MyDatabaseHelper(@Nullable Context context, String DATABASE_NAME, int DATABASE_VERSION) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    /*  * DATABASE_NAME 로 만든 DB가 없으면
    *       - onCreate() 를 callback  >>  onOpen() 호출
    *         (생성할 때 VERSION 정보를 같이 명시한다.)
    *   * DATABASE_NAME 로 만든 DB가 있다면
    *       - onOpen() 호출
    *
    *       - if(Name으로 DB가 있을때 && Version이 다를 때)
    *           onUpgrade() 호출
    *           (기존 데이터베이스의 스키마를 변경하는 작업 수행)
    *           (앱이 업데이트 되어 배포될 때, DB 스키마를 다시 생성하는데 사용된다)
    *           (기존의 DB를 Drop 하고 새로운 DB를 만드는 작업 수행)
    * */

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DBTest", "onCreate()");
        String sql = "CREATE TABLE IF NOT EXISTS person"
                + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, age INTEGER, mobile TEXT)";
        db.execSQL(sql);
        Log.i("DBTest", "onCreate() the End");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.i("DBTest", "onOpen()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DBTest", "onUpgrade()");

    }
}

public class Example22_SQLiteHelperActivity extends AppCompatActivity {

    private EditText _22_dbNameEt;
    private EditText _22_tableNameEt;
    private EditText _22_empNameEt;
    private EditText _22_empAgeEt;
    private EditText _22_empMobileEt;
    private TextView _22_resultTv;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example22_sqlite_helper);

        _22_dbNameEt = findViewById(R.id._22_dbNameEt);
        _22_tableNameEt = findViewById(R.id._22_tableNameEt);

        // _22_dbCreateBtn
        Button _22_dbCreateBtn = findViewById(R.id._22_dbCreateBtn);
        _22_dbCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dbName = _22_dbNameEt.getText().toString();

                MyDatabaseHelper helper = new MyDatabaseHelper(Example22_SQLiteHelperActivity.this,
                        dbName, 2);
                // 읽기전용 DB  - Helper 를 이용해 database reference 를 획득
                database = helper.getWritableDatabase();
                /*
                1. 새로운 DB 생성
                    helper가 생성되면서 Database 생성
                    onCreate()  >>  onOpen()
                */

            }
        }); // _22_dbCreateBtn
    }
}
