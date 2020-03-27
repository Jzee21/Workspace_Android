package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Example16_ServiceDataTransferActivity extends AppCompatActivity {

    private TextView dataFromServiceTv;
    private EditText dataToServiceEt;
    private Button dataToServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example16_service_data_transfer);

        dataFromServiceTv = (TextView) findViewById(R.id.dataFromServiceTv);
        dataToServiceEt = (EditText) findViewById(R.id.dataToServiceEt);
        dataToServiceBtn = (Button) findViewById(R.id.dataToServiceBtn);

        dataToServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent i = new Intent(getApplicationContext(),
                        Example16sub_DataTransferService.class);
                i.putExtra("DATA", dataToServiceEt.getText().toString());
                startService(i);
            }
        });

    }// onCreate()

    /*  Service로부터 intent가 도착하면 이 method를 호출    */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String result = intent.getExtras().getString("RESULT");
        dataFromServiceTv.setText(result);

    }
}
