package com.multi.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
*   Context  : private data
*
*   1차 권한
*   manifest
*   <uses-permission android:name="android.permission.READ_CONTACTS" />     주소록 읽기
*   <uses-permission android:name="android.permission.WRITE_CONTACTS" />    주소록 쓰기
*
*   2차 권한
*
*/
public class Example24_ContactActivity extends AppCompatActivity {

    private TextView _24_resultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example24_contact);

        // _24_resultTv
        _24_resultTv = findViewById(R.id._24_resultTv);

        // 보안설정 먼저
        // 1. Android Version 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // M 버전 이상
            // 2. 사용자 권한 확인 (주소록 읽기)
            int permissionResult = ActivityCompat.checkSelfPermission(
                    getApplicationContext(), android.Manifest.permission.READ_CONTACTS);
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                // 권한이 없다면
                // 3. 사용자에게 권한을 처음 요청하는지 확인
                if (shouldShowRequestPermissionRationale(
                        android.Manifest.permission.READ_CONTACTS)) {
                    // True  >>  거부한 이력이 있다
                    // 4. 권한을 다시 요청
                    AlertDialog.Builder dialog =
                            new AlertDialog.Builder(Example24_ContactActivity.this);
                    dialog.setTitle("권한이 필요합니다.");
                    dialog.setMessage("주소록 읽기 권한이 필요합니다. 수락하시겠습니까?");
                    dialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(
                                            new String[]{android.Manifest.permission.READ_CONTACTS},
                                            200);
                                    // callback onRequestPermissionsResult()
                                }
                            });
                    dialog.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 권한 설정 거부
                                    // do nothing
                                }
                            });
                    dialog.create().show();
                // 3. 권한 요청 기록 확인
                } else {
                    // False  >>  처음 권한을 요청한 경우
                    requestPermissions(
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            200);
                    // callback onRequestPermissionsResult()
                }
            // 2. 권한 확인
            } else {
                // 권한이 있다면
                Log.i("ContactTest", "보안설정 통과");
            }
        // 1. 버전 확인
        } else {
            // M 버전 미만
            Log.i("ContactTest", "보안설정 통과");
        }

        //_24_getContextBtn
        Button _24_getContextBtn = findViewById(R.id._24_getContextBtn);
        _24_getContextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ContactTest", "Btn Click");
                processContact();
            }
        });

    } // onCreate()

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("ContactTest", "보안설정 통과");
            }
        }

    }

    /*
    *   한명이 여려개의 전화번호를 가지는 경우가 있다
    *   따라서, 사람과 번호들 테이블이 구분되어있다
    */
    private void processContact() {

        // 사람 목록
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null,   // 모든 컬럼
                null,    // 조건절 : 조건 없이
                null, // 조건절에 사용할 값
                null);  // 정렬 방향

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Log.i("ContactTest", "[id, name] : " + id + ", " + name);

            // String msg = "Name : " + name + "\n";

            Cursor infoCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null, null);
            while (infoCursor.moveToNext()) {
                String mobile = infoCursor.getString(
                        infoCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                // msg += "\t Number : " + mobile + "\n";
                String msg = "Name : " + name + ", Number : " + mobile + "\n";
                _24_resultTv.append(msg);
            }
            // _24_resultTv.append(msg);

        } // while
    }
}
