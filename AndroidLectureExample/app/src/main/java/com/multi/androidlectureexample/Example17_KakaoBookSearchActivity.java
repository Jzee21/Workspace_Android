package com.multi.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Example17_KakaoBookSearchActivity extends AppCompatActivity {

    private EditText kakaoBookEt;
    private Button kakaoBookBtn;
    private ListView kakaoBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example17_kakao_book_search);

        kakaoBookEt = (EditText) findViewById(R.id.kakaoBookEt);
        kakaoBookBtn = (Button) findViewById(R.id.kakaoBookBtn);
        kakaoBookList = (ListView) findViewById(R.id.kakaoBookList);

        kakaoBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        Example17sub_KakaoBookSearchService.class);
                i.putExtra("KEYWORD", kakaoBookEt.getText().toString());
                startService(i);
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

//        String[] booklists= intent.getExtras().getStringArray("BOOKLIST");
        ArrayList<String> resultData =
                (ArrayList<String>) intent.getExtras().get("BOOKLIST");

        ArrayAdapter adapter =
                new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        resultData);
        kakaoBookList.setAdapter(adapter);
    }
}
