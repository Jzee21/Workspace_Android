package com.multi.androidlectureexample;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class Example23sub_PersonContentProvider extends ContentProvider {

    /*  URI 형식 ( Content Provider를 찾기위한 특별한 문자열 )
        content://{Authority}/{BASE_PATH}/{#no}
        - BASE_PATH : Table 명
        - #no       : recode 번호

        ex) content://com.multi.person.provider/person/1
     */

    private SQLiteDatabase database;

    public Example23sub_PersonContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        // throw new UnsupportedOperationException("Not yet implemented");

        Log.i("DBTest", "CP_insert()");

        // database 에 insert 하는 방법 중 하나 (SQL 문 없이)
        // ContentValues values  : key, value 형태로 데이터 묘사
        database.insert("person",null, values);
        return uri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        // return false;

        // 앱 실행 시 Android System에 의해서 Content Provider가 자동적으로 생성된다.
        // 여기에 Database 생성 및 Table 생성 코드가 오면 된다
        // 또한, DB Reference를 획득하는 코드도 오면 된다.
        Log.i("DBTest", "CP_onCreate()");

        PersonDatabaseHelper helper = new PersonDatabaseHelper(getContext());
        database = helper.getWritableDatabase();

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
