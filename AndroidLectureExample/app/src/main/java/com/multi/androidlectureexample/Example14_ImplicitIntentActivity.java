package com.multi.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Example14_ImplicitIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example14_implicit_intent);

        Button implicitIntentBtn = (Button) findViewById(R.id.implicitIntentBtn);
        implicitIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("Implicit", "Btn Click");
                // implicit Intent(묵시적 인텐트)
                Intent i = new Intent();
                i.setAction("MY_ACTION");
                i.addCategory("INTENT_TEST");
                i.putExtra("SEND DATA", "안녕?");
                startActivity(i);
            }
        });

        final Button dialBtn = (Button) findViewById(R.id.dialBtn);
        dialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:01023456789"));
                startActivity(i);
            }
        });

        Button browserBtn = (Button) findViewById(R.id.browserBtn);
        browserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.naver.com"));
                startActivity(i);
            }
        });

        /*  전화걸기 기능은 AndroidManifest.xml 파일에 권한 설정이 필요하다
             - Android 6.0 (Marshmallow) 이상에서는 manifest 파일에 기술하는 것 이외에
                추가적인 권한을 요구한다
            권한 자체가 크게 2가지로 분류된다
             - 일반 권한(normal permission)
             - 위험 권한(dangerous permission)
                : 위치, 전화, 카메라, 마이크, 문자, 일정, 주소록, 센서
            특정 앱을 사용할 때 앱이 사용자에게 권한을 요구하고
            사용자가 권한을 허용하면 그 기능을 이용할 수 있다.
            설정 > Application > 선택한 앱 > 권한 에는 앱이 요청하는 권한의 목록과 허가 여부 수정 가능
        */
        Button callBtn = (Button) findViewById(R.id.callBtn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자의 Android Version이 M 이상인지 확인
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Android 6.0 ~
                    // Permission Check : Manifest.permission.CALL_PHONE
                    int permissionResult =
                            ActivityCompat.checkSelfPermission(getApplicationContext(),
                                    Manifest.permission.CALL_PHONE);
                    if (permissionResult == PackageManager.PERMISSION_DENIED) {
                        // PackageManager.PERMISSION_DENIED : -1
                        // not allowed
                        // 권한 설정을 거부한 적이 있는지, 그렇지 않은지 확인
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                            // Boolean shouldShowRequestPermissionRationale()

                            // 이전에 거부당한 권한을 다시 요청하는 경우
                            AlertDialog.Builder dialog =
                                    new AlertDialog.Builder(Example14_ImplicitIntentActivity.this);
                            dialog.setTitle("권한을 요청합니다");
                            dialog.setMessage("전화걸기 권한이 필요합니다. 수락할까요?");
                            dialog.setPositiveButton("네",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                                    1000);
                                        }
                                    });
                            dialog.setNegativeButton("아니오",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(Example14_ImplicitIntentActivity.this,
                                                    "권한 요청이 거부되었습니다.\n해당 기능을 사용할 수 없습니다.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            dialog.show();

                        // (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE))
                        } else {
                            // 권한을 거부한 적이 없음  - 권한 요청이 처음
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                    1000);
                            // >> onRequestPermissionsResult() 메서드가 호출된다.
                        }

                    // (permissionResult == PackageManager.PERMISSION_DENIED)
                    } else {
                        // get allowed
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_CALL);
                        i.setData(Uri.parse("tel:0102345-6789"));
                        startActivity(i);
                    }

                // (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                } else {
                    // ~ Android 5.1
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:0102345-6789"));
                    startActivity(i);
                }
            }
        }); // callBtn.setOnClickListener()

    } // onCreate()

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 다수의 requestPermissions() 중 누가 호출했는지 구분
        if (requestCode == 1000) {

            // 유저의 권한요청의 응답 갯수가 1개 이상인지,
            // 첫번째 권한요청이 허용 되었는지 확인
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //
                Intent i = new Intent();
                i.setAction(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:01024320954"));
                startActivity(i);
            } else if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(Example14_ImplicitIntentActivity.this,
                        "권한 요청이 거부되었습니다.\n해당 기능을 사용할 수 없습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }// onRequestPermissionsResult()
}
