package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
*   Android는 Embedded 형태로 개발된 DBMS 하나를 내장하고 있다.
*   이 경량화된 DBMS를 SQLite라고 한다
*   파일로 이루어져있기 때문에 Database 의 복사, 이동같은 처리가 쉽고
*   속도가 빠른 표준 SQL을 지원한다.
*
*   Database를 생성하고 Table 을 만든 후 Data를 Insert 한다
*
*/
public class Example21_SQLiteBasicActivity extends AppCompatActivity {

    private EditText _21_dbNameEt;
    private EditText _21_tableNameEt;
    private EditText _21_empNameEt;
    private EditText _21_empAgeEt;
    private EditText _21_empMobileEt;
    private TextView _21_resultTv;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example21_sqlite_basic);

        _21_dbNameEt = findViewById(R.id._21_dbNameEt);
        _21_tableNameEt = findViewById(R.id._21_tableNameEt);

        // _21_dbCreateBtn
        Button _21_dbCreateBtn = findViewById(R.id._21_dbCreateBtn);
        _21_dbCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dbName = _21_dbNameEt.getText().toString();
                database = openOrCreateDatabase(dbName, MODE_PRIVATE, null);
                /*  MODE_PRIVATE : 0x0000
                *   일반적인 형태(읽고쓰기가 가능한) DB 를 생성하거나 Open 하는 의미    */
                //  result : /data/data/{app_package}/databases/{dbName}.db (file)
                Log.i("DBTest", "Create DB : " + database);
            }
        }); // _21_dbCreateBtn

        // _21_tableCreateBtn
        Button _21_tableCreateBtn = findViewById(R.id._21_tableCreateBtn);
        _21_tableCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableName = _21_tableNameEt.getText().toString();

                if (database == null) {
                    Log.i("DBTest", "! Please Create DB, First");
                    Toast.makeText(getApplicationContext(),
                            "Database를 먼저 생성해주세요", Toast.LENGTH_SHORT).show();
                    _21_dbNameEt.requestFocus();
                    return;
                }

                String sql = "CREATE TABLE IF NOT EXISTS " + tableName
                        + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "name TEXT, age INTEGER, mobile TEXT)";
                database.execSQL(sql);

                Log.i("DBTest", "Success! Create Table");
            }
        }); // _21_tableCreateBtn

        // _21_empInsertBtn
        _21_empNameEt   = findViewById(R.id._21_empNameEt);
        _21_empAgeEt    = findViewById(R.id._21_empAgeEt);
        _21_empMobileEt = findViewById(R.id._21_empMobileEt);

        Button _21_empInsertBtn = findViewById(R.id._21_empInsertBtn);
        _21_empInsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empName = _21_empNameEt.getText().toString();
                int empAge = Integer.parseInt(_21_empAgeEt.getText().toString());
                String empMobile = _21_empMobileEt.getText().toString();

                if (database == null) {
                    Log.i("DBTest", "! Please Open DB, First");
                    Toast.makeText(getApplicationContext(),
                            "Database를 먼저 열어주세요", Toast.LENGTH_SHORT).show();
                    _21_dbNameEt.requestFocus();
                    return;
                }

                String sql = "INSERT INTO emp(name, age, mobile) VALUES "
                        + "('" + empName + "', " + empAge + ", '" + empMobile + "')";
                database.execSQL(sql);
                Log.i("DBTest", "Success! Insert emp");

                _21_empNameEt.setText("");
                _21_empAgeEt.setText("");
                _21_empMobileEt.setText("");

                _21_empNameEt.requestFocus();

            }
        }); // _21_empInsertBtn

        // _21_empSelectBtn
        _21_resultTv = findViewById(R.id._21_resultTv);

        Button _21_empSelectBtn = findViewById(R.id._21_empSelectBtn);
        _21_empSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "SELECT _id, name, age, mobile FROM emp";

                if (database == null) {
                    Log.i("DBTest", "! Please Open DB, First");
                    Toast.makeText(getApplicationContext(),
                            "Database를 먼저 열어주세요", Toast.LENGTH_SHORT).show();
                    _21_dbNameEt.requestFocus();
                    return;
                }
                /*
                execSQL()   : select 계열이 아닌 SQL 문장을 실행할 때 사용
                rawQuery()  : select 계열의 SQL 문장을 실행할 때 사용
                */
                Cursor cursor = database.rawQuery(sql, null);

                _21_resultTv.setText("");
                _21_resultTv.append(database.toString() + "\n");
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    int age = cursor.getInt(2);
                    String mobile = cursor.getString(3);

                    String result = "recode : " + id + ", " + name + ", " + age + ", " + mobile;
                    _21_resultTv.append(result + "\n");
                }
            }
        }); //_21_empSelectBtn

    }
}
/*
CREATE TABLE IF NOT EXISTS emp(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT       // varchar
    age INTEGER
    mobile TEXT)
* */
/*
INSERT INTO emp(name, age, mobile)
VALUES ('aaa', 20, '010-0000-0000')
*/