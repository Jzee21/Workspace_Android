package com.multi.androidlectureexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // _01_linearlayoutBtn
        Button _01_linearlayoutBtn = (Button) findViewById(R.id._01_linearlayoutBtn);
        _01_linearlayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Target Activity를 찾아서 실행
                /*  2가지 방식으로 activity를 찾을 수 있다.
                    (explicit / implicit 방식)
                    (intent 객체가 필요하다)   */

                // explicit 방식
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example01_LayoutActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _02_WidgetBtn
        Button _02_WidgetBtn = (Button) findViewById(R.id._02_widgetBtn);
        _02_WidgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example02_WidgetActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _03_EventBtn
        Button _03_EventBtn = (Button) findViewById(R.id._03_eventBtn);
        _03_EventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example03_EventActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _04_ActivityEventBtn
        Button _04_ActivityEventBtn = (Button) findViewById(R.id._04_activityEventBtn);
        _04_ActivityEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example04_TouchEventActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        //_05_swipeEventBtn
        Button _05_SwipeEventBtn =
                (Button) findViewById(R.id._05_swipeEventBtn);
        _05_SwipeEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example05_SwipeActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _06_SendMessageBtn
        Button _06_SendMessageBtn = (Button) findViewById(R.id._06_SendMessageBtn);
        _06_SendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // alert창(dialog)를 이용해서 문자열을 입력받고
                // 입력받은 문자열을 다음 Activity로 전달

                // 사용자가 문자열을 입력받을 수 있는 widget을 하나 생성
                final EditText editText = new EditText(MainActivity.this);
                // AlertDialog 하나 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Activity 데이터 전달");
                builder.setMessage("다음 Activity에 전달할 내용을 입력하세요.");
                builder.setView(editText);
                builder.setPositiveButton("전달", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent();
                        ComponentName cname =
                                new ComponentName("com.multi.androidlectureexample",
                                        "com.multi.androidlectureexample.Example06_SendMessageActivity");
                        i.setComponent(cname);
                        // 데이터 전달
                        i.putExtra("sendMSG", editText.getText().toString());
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 취소 버튼이기 때문에 특별한 이벤트 처리가 필요 없다
                    }
                });
                builder.show();
            }
        });

        //_07_DataFromBtn
        Button _07_DataFromBtn = (Button) findViewById(R.id._07_DataFromBtn);
        _07_DataFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example07_DataFromActivity");
                i.setComponent(cname);
                // 새로 생성되는 Activity로부터 데이터를 받아오기 위한 용도
                // 두번째 Activity가 finish되는 순간 데이터를 받아온다.
                // startActivity(i);
                startActivityForResult(i, 3000);
                // requestCode : Activity를 호출한 Activity를 구분하기 위한 코드
                // onActivityResult()를 이용해 반환된 Intent를 받는다.
            }
        });

        // _08_ANRBtn
        // try make ANR
        Button _08_ANRBtn = (Button) findViewById(R.id._08_ANRBtn);
        _08_ANRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example08_ANRActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _09_CounterLogBtn
        // try make ANR
        Button _09_CounterLogBtn = (Button) findViewById(R.id._09_CounterLogBtn);
        _09_CounterLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example09_CounterLogActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _10_CounterLogProgressBtn
        // cal doing,
        // set ProgressBar - Inner Thread
        Button _10_CounterLogProgressBtn = (Button) findViewById(R.id._10_CounterLogProgressBtn);
        _10_CounterLogProgressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example10_CounterLogProgressActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _11_CounterLogHandlerBtn
        // set ProgressBar - Other Thread with Handler
        Button _11_CounterLogHandlerBtn = (Button) findViewById(R.id._11_CounterLogHandlerBtn);
        _11_CounterLogHandlerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example11_CounterLogHandlerActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _12_BookSearchSimpleBtn
        // get list of book
        // from Web Application
        // by keyword
        Button _12_BookSearchSimpleBtn = (Button) findViewById(R.id._12_BookSearchSimpleBtn);
        _12_BookSearchSimpleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example12_BookSearchSimpleActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _13_BookSearchDetailBtn
        Button _13_BookSearchDetailBtn = (Button) findViewById(R.id._13_BookSearchDetailBtn);
        _13_BookSearchDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example13_BookSearchDetailActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _14_ImplicitIntentBtn
        Button _14_ImplicitIntentBtn = (Button) findViewById(R.id._14_ImplicitIntentBtn);
        _14_ImplicitIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit 방식 Activity 호출 (명시적 Intent)
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example14_ImplicitIntentActivity");
                i.setComponent(cname);
                startActivity(i);

            }
        });

        /*  App이 실행되었을 때 항상 Activity가 보이는 것은 아니다.
            가장 대표적인 경우 : 카카오톡, 멜론, 벅스 등...
            즉, Service는 화면이 없는 Activity라고 생각하자...?

            Activity : onCreate() > onStart() > onResume() > onPause > onStop()
            Service  : onCreate() > onStartCommand() > onDestroy()
            - 눈에 보이지 않기 때문에 background 에서 로직처리하는데 주로 사용된다.
        */
        // _15_ServiceLifecycleBtn
        Button _15_ServiceLifecycleBtn = (Button) findViewById(R.id._15_ServiceLifecycleBtn);
        _15_ServiceLifecycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example15_ServiceLifecycleActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _16_ActivityServiceDataBtn
        Button _16_ActivityServiceDataBtn = (Button) findViewById(R.id._16_ActivityServiceDataBtn);
        _16_ActivityServiceDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example16_ServiceDataTransferActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _17_KakaoBookSearchBtn
        Button _17_KakaoBookSearchBtn = (Button) findViewById(R.id._17_KakaoBookSearchBtn);
        _17_KakaoBookSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example17_KakaoBookSearchActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _18_BRTestBtn
        Button _18_BRTestBtn = (Button) findViewById(R.id._18_BRTestBtn);
        _18_BRTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example18_BRTestActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });

        // _19_BRSMSBtn
        Button _19_BRSMSBtn = (Button) findViewById(R.id._19_BRSMSBtn);
        _19_BRSMSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName cname =
                        new ComponentName("com.multi.androidlectureexample",
                                "com.multi.androidlectureexample.Example19_BRSMSActivity");
                i.setComponent(cname);
                startActivity(i);
            }
        });



    }   // onCreate()


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
        //_07_DataFromBtn
        if(requestCode==3000 && resultCode==7000) {
            String msg = (String) data.getExtras().get("ResultValue");
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
