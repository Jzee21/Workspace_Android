package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    // Activity의 Life cycle
    // activity는 사용자에 의해서 이벤트가 발생되면 그 상태가 변한다.
    // 그에 따라 callback method가 호출되는데 그 callback method에 대해 알아야한다.
    /*
    *    1. Activity는 class 상태로 존재
    *    2. Activity가 화면에 나타나려면 객체화가 되어야 한다(instance화)
    *    3. onCreate() method가 callback된다 >> 화면구성을 주로 한다
    *    4. onStart() method가 callback된다  >> Activity의 초기화 작업을 한다
    *    5. Activity가 foreground로 나타나면서 사용자와 interaction이 가능
    *    6. onResume() method가 callback된다
    *    7. Activity가 running 상태가 된다
    *    8. Activity의 일부분이 보이지 않는 상태(Pause 상태)
    *    9. Pause 상태가 되면 onPause() method가 callback된다
    *   10. Activity의 전체가 완전히 가려져서 보이지 않는 상태가 된다(Stop 상태)
    *   11. Stop 상태가 되면 onStop() method가 callback된다
    *   12. 만약 Stop 상태에서 다시 Running 상태가 되면
    *   13. onRestart() -> onStart() -> onResume() 순서로 다시 호출
    *   14. 사용하고 있는 activity를 종료하게되면 killed 상태로 진입
    *   15. 진입하기 전에 onDestory() method가 callback된다
    *           - 사용한 리소스 초기화, 반납
    */

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MYTEST", "Call : onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MYTEST", "Call : onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MYTEST", "Call : onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MYTEST", "Call : onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MYTEST", "Call : onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MYTEST", "Call : onDestroy()");
    }

    // onCreate()
    //  callback method
    //  특정 시점이 되면 Android System에 의해서 자동적으로 호출된다.
    //  (해당 클래스의 instance가 생성될 때)(activity가 생성될 때)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // activity의 화면을 구성하는 방식은 크게 2가지 방식이다.
        // 1. Java Code로 Widget Component를 생성해서 화면에 붙이는 방식
        // 2. XML 파일을 이용해서 화면구성을 처리
        //    - xml을 이용하는 방식은 표현(화면구성-UI)과 구현(로직)을 분리

        // res 폴더
        // 1. drawable  : application 에서 사용하는 그림 파일 저장
        // 2. layout    : Activity에서 사용할 화면구성을 위한 xml 파일 저장
        // 3. mipmap    : launcher icon과 같은 이미지 자원을 저장
        // 4. values    : 문자열이나 컬러와 같은 다양한 자원에 대한 정보를 저장

        // R.java는 android에 의해서 자동으로 생성되는 class
        // activity_main.xml 파일을 이용해서 activity의 View를 설정하는 method
        //  - xml 파일명은 무조건 소문자로 만들어야 한다.
        setContentView(R.layout.activity_main);
        // xml 파일의 내용
        // 화면을 구성할 수 있는 component가 너무 많다.
        // View와 View Group
        // View : 통상적으로 눈에 보이는 Component
        //      - Button, TextView(lable), ImageView(그림), ...
        // View Group : View의 크기와 위치를 조절해서 설정한다.
        //      - 대표적인 녀석이 Layout


        // Debuging
        // Logcat을 이용한다.
        Log.i("MYTEST", "Call : onCreate()");

    }
}
