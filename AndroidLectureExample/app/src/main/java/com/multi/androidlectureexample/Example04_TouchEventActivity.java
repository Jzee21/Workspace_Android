package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Example04_TouchEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example04_touchevent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Toast Event 이용
        Toast.makeText(this, "소리없는 아우성!!", Toast.LENGTH_SHORT).show();
        Log.i("MyTEST", "Touch");

        return super.onTouchEvent(event);
    }

}
