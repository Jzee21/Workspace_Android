package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Example14sub_ImplicitIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example14sub_implicit_intent);

        Intent i = getIntent();
//        i.getExtras();
        Toast.makeText(getApplicationContext(),
                i.getExtras().getString("SEND DATA"), Toast.LENGTH_SHORT).show();
    }
}
