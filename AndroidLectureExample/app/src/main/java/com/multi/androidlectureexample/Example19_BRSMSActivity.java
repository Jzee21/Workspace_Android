package com.multi.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/*
*   1. SMS 처리는 상당히 개인적인 정보  >>  보안처리
*       AndroidManifest.xml 에 기본 보안에 대한 설정이 필요
*       <uses-permission android:name="android.permission.RECEIVE_SMS" />
*
*   2. Broadcast Receiver Component 생성
*       외부 Component 생성
*       AndroidManifest.xml 에 자동 등록
*       >>  수신할 Broadcast를 <intent-filter>로 추가
*
*   3. SMS Broadcast를 수신하여 Activity에게 전달
*       - Activity 실행 시 보안설정 요청
*
*/
public class Example19_BRSMSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example19_br_sms);

        TextView _19_smsSender = (TextView) findViewById(R.id._19_smsSenderTv);
        TextView _19_smsMessage = (TextView) findViewById(R.id._19_smsMessageTv);
        TextView _19_smsDate = (TextView) findViewById(R.id._19_smsDateTv);

        // SMS 보안처리  >>  권한 요청
        // 1. Android Version M~ 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // M 버전 이상
            // 2. 사용자 권한 중 SMS 받기 권한의 설정 확인
            int permissionResult = ActivityCompat.checkSelfPermission(
                    getApplicationContext(),
                    Manifest.permission.RECEIVE_SMS);
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                // 권한이 없다면
                // 3. 사용자에게 권한을 처음 요청하는지 확인
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)) {
                    // True  >>  처음이 아닌 경우 (거부한적이 있다)
                    // 4. 권한을 다시 요청
                    AlertDialog.Builder dialog =
                            new AlertDialog.Builder(Example19_BRSMSActivity.this);
                    dialog.setTitle("권한이 필요합니다.");
                    dialog.setMessage("SMS 수신 권한이 필요합니다. 수락하시겠습니까?");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(
                                    new String[]{Manifest.permission.RECEIVE_SMS},
                                    100);
                            // call onRequestPermissionsResult()
                        }
                    });
                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 권한 설정 거부
                            // do nothing
                        }
                    });
                    dialog.create().show();
                } else {
                    // False  >>  처음 권한을 요청한 경우
                    requestPermissions(
                            new String[]{Manifest.permission.RECEIVE_SMS},
                            100);
                    // call onRequestPermissionsResult()
                }

            } else {
                // 권한이 있다면
                Log.i("SMSTest", "보안설정 통과");
            }

        } else {
            // M 버전 미만
            // AndroidManifest 에서
            // <uses-permission android:name="android.permission.RECEIVE_SMS" />
            // 를 추가한 것으로 보안설정 완료
            Log.i("SMSTest", "보안설정 통과");
        }
        // 보안설정 확인

    } // onCreate()

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 사용자가 권한을 설정을 완료하면 이 부분이 호출된다.
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 사용자가 권한을 허용
                Log.i("SMSTest", "보안설정 통과");
            }
        }
    } // onRequestPermissionsResult()
}
