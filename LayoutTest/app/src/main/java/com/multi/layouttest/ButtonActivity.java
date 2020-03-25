package com.multi.layouttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
    Event 처리
    Event : 사용자 또는 시스템에 의해서 발생되는 모든 것
    Event Handling이 필요하다
        Java - Event Delegation Model을 이용해서 event 처리
            Event 처리에 관련된 3가지 객체가 존재
                1. Event Source 객체 : Event가 발생한 객체를 지칭
                2. Event Handler 객체(Listener) : Event를 처리하는 객체
                3. Event 객체 : 발생된 Event에 대한 세부정보를 가진 객체
                (Event Source는 이미 생성됨, Event 객체는 자동적으로 생성)
            Event Source에 Event Handler를 부착시켜 Event가 발생되면
            부착된 Handler를 통해 Event를 처리

 */
public class ButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        // 1. Event Source instance
        Button myBtn = (Button) findViewById(R.id.eventBtn);
        final TextView tv = (TextView) findViewById(R.id.myTv);
        // 상수화 시켜 heap 영역에 존재하도록 하여
        // onCreate()가 종료되어도 변수가 존대하도록 한다.(생존시간 증가)

        // 2. Event Handler instance - create
//        MyEventHandler handler = new MyEventHandler(tv);
        // 3. Event Source에 Event Handler 부착
//        myBtn.setOnClickListener(handler);

        // 2+3. Event Source에 Event Handler 객체를 생성헤서 부착
        myBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("Clicked");
            }
        });

    }

}

/*
    여기서 필요한 것은 일반 클래스의 객체가 아닌
    이벤트를 처리할 수 있는 특수한 능력을 가지고 있는 리스너 객체가 필요
    (즉, 특수한 interface를 구현한 클래스여야 한다)
    (특수한 interface는 여러개가 존재한다)(Event 종류에 따라서)
 */
/*
    아래와 같이 만들었으나...
    실제 구현에서는 anonymous inner class(익명 이너클래스)를 이용하기 때문에
    아래와 같은 외부 class는 사용하지 않는다.
 */
/*
class MyEventHandler implements View.OnClickListener {

    private TextView tv;

    MyEventHandler() {  }

    MyEventHandler(TextView tv) {
        this.tv = tv;
    }

    @Override
    public void onClick(View v) {
        // Event 처리 코드
        //Log.i("MyEvent", "버튼이 눌렸다");
        tv.setText("Clicked");
    }

}*/
