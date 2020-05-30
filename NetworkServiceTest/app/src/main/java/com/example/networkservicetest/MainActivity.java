package com.example.networkservicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView main_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_textview = (TextView) findViewById(R.id.main_textview);

        Intent intent = new Intent(getApplicationContext(), ClientService.class);
        startService(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String data = intent.getExtras().getString("RESULT");
        main_textview.append(data + "\n");

    }
}
