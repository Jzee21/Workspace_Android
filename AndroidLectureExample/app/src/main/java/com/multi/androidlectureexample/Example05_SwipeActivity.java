package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Example05_SwipeActivity extends AppCompatActivity {

    //private TextView tv;
    private LinearLayout ll;
    //private float[] point1 = new float[2];
    //private float[] point2 = new float[2];

    private double x1, x2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example05_swipe);

        //tv = (TextView) findViewById(R.id.TouchEvent);
        //
        ll = (LinearLayout) findViewById(R.id.myLinearlayout);
        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String msg = "";
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    x1 = event.getX();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    x2 = event.getX();
                    if (x1 > x2) {
                        msg = "Left Swipe";
                    } else {
                        msg = "Right Swipe";
                    }
                    Toast.makeText(Example05_SwipeActivity.this,
                            msg, Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Toast Event 이용
        //Toast.makeText(this, "소리없는 아우성!!", Toast.LENGTH_SHORT).show();
        //Log.i("MyTEST", "Touch");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                tv.setText("터치 DOWN");
                point1[0] = event.getX();
                point1[1] = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                tv.setText("터치 MOVE");
                break;

            case MotionEvent.ACTION_UP:
                tv.setText("터치 UP");
                point2[0] = event.getX();
                point2[1] = event.getY();
                if(point1[0]>point2[0]) {
                    Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onTouchEvent(event);
    }
    */

}
