package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class Example07_DataFromActivity extends AppCompatActivity {

    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example07_datafrom);

        // reference 획득
        Spinner spinner = (Spinner) findViewById(R.id.mySpinner);
        Button sendBtn = (Button) findViewById(R.id.sendDataBtn);

        // Spinner 안에 데이터 항목을 추가
        // 방법은 여러가지다. 일단 쉽게. 데이터를 문자열로 가정(그림도 가능하거든)
        final ArrayList<String> list = new ArrayList<String>();
        list.add("포도");         // 원래는 DB에서 받아온 데이터 들이 들어가겠지?
        list.add("딸기");
        list.add("바나나");
        list.add("사과");

        // adapter를 이용해서 Spinner에 List를 추가한다.
        // 종류가 다양. 그중 ArrayList에 잘 맞는 Adapter 사용
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);

        // spinner의 값을 전달하기 위해서는....
        // spinner의 값이 선택될 때 itemSelected 이벤트를 이용해 값을 저장하고
        // button onClick 이벤트를 이용해 값을 돌려보낸다.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // here
                result = list.get(position);
                // Log.i("SELECTED", result+"가 선택되었습니다.");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ResultValue", result);
                setResult(7000, returnIntent);
                Example07_DataFromActivity.this.finish();
            }
        });

    }
}
