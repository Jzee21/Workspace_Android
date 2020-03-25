package com.multi.layouttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/*
    Activity는 App을 구성하고 있는 화면을 지칭
    화면을 표현하고 관리하기 위한 class

    화면을 표현할 때는 Activity 하나와 하나 이상의 xml 파일이 필요하다
    Activity 내에서 자바코드로 화면구성을 할 수는 있으나, 권장되지 않음
    (표현과 구현이 분리되지 않기 때문)
    App은 일반적으로 여러개의 Activity로 구성

    xml에 여러가지 widget을 넣어서 사용자 component를 표현할 수 있다
    widget을 원하는 크기와 위치에 표현하기 위해서
    widget을 관리하는 component가 따로 필요하다 -> LayoutManager
    - LinearLayout, GridLayout...

    LinearLayout
*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
