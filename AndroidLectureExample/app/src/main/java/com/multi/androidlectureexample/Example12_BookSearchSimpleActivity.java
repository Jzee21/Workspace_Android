package com.multi.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Example12_BookSearchSimpleActivity extends AppCompatActivity {

    private String[] titleArr;
    private String[] isbnArr;
    private ArrayAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example12_book_search_simple);

        Button searchBtn = (Button) findViewById(R.id.searchBtn);
        final EditText searchTitle = (EditText) findViewById(R.id.searchTitle);
        final ListView searchList = (ListView) findViewById(R.id.searchList);

        /*
            Network 연결(Web Application)을 해야하기 때문에
            UI Thread(Activity)에서 이 작업을 해서 안된다
            >> Thread로 해결
            >> Handler를 이용
         */
        @SuppressLint("HandlerLeak")
        final Handler handler = new Handler() {
            /*  Thread에 의해서 추후 sendMessage가 호출되는데
                sendMessage에 의해서 전달된 데이터를 처리하기 위해서
                handleMessage를 override 하면서 instance를 생성    */
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

//                Bundle bundle = msg.getData();
//                titleArr = (String[]) bundle.get("BOOKLIST");
//                isbnArr = (String[]) bundle.get("BOOKIDLIST");

                /*  ListView 사용은 Spinner와 비슷
                *   ListView에 데이터를 설정하려면 Adapter가 필요
                *   Adapter에 데이터를 설정헤 ListView에 붙인다         */
                //
//                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
//                                android.R.layout.simple_list_item_1,
//                                bookList);
//                searchList.setAdapter(adapter);


                // get List & ISBN
                Bundle bundle = msg.getData();

                try {
                    if(adapter != null) {
                        titleArr = null;
                        isbnArr = null;
                    }
                    titleArr = (String[]) bundle.get("BOOKLIST");
                    isbnArr = (String[]) bundle.get("BOOKIDLIST");

                    adapter = new ArrayAdapter(getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            titleArr);
                    searchList.setAdapter(adapter);
                } catch (Exception e) {
                    Log.i("bookList", "adapter claen] " + e);
                }

                Log.i("bookList", "adapter] " + adapter);

            }
        };

        // Button Event
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchTitle.getText().toString();
                Log.i("keyword", keyword);
                if (keyword.trim().getBytes().length <= 0) {
                    Toast.makeText(Example12_BookSearchSimpleActivity.this,
                            "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (keyword != null) {
                    BookSearchRunnable runnable =
                            new BookSearchRunnable(handler, keyword);
                    Thread t = new Thread(runnable);
                    t.start();
                }
            }
        });

        try {
            searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(Example12_BookSearchSimpleActivity.this,
//                            isbnArr[position], Toast.LENGTH_SHORT).show();
                    // 12 : 0~11
                    Intent i = new Intent();
                    ComponentName cname =
                            new ComponentName("com.multi.androidlectureexample",
                                    "com.multi.androidlectureexample.BookDetailActivity");
                    i.setComponent(cname);
                    // 데이터 전달
                    i.putExtra("bookTitle", titleArr[position]);
                    i.putExtra("bookIsbn", isbnArr[position]);
                    startActivity(i);
                }
            });
        } catch (Exception e) {
            Log.i("ActivityError", "onItemClick : " + e);
        }

    }
}

// Thread
class BookSearchRunnable implements Runnable {

    private Handler handler;
    private String keyword;

    BookSearchRunnable() {}
    BookSearchRunnable(Handler handler, String keyword) {
        this.handler = handler;
        this.keyword = keyword;
    }

    @Override
    public void run() {
        // Web Application 호출
        // 결과를 Activity에서 사용하기 편하게 가공 후 Handler를 이요해 Activity에 전달

        // 1. 접속할 url
//        String url = "http://70.12.60.91:8080/bookSearch/searchTitle?keyword=" + keyword;
        String url = "http://70.12.60.91:8080/bookSearch/searchList?keyword=" + keyword;
        // 2. Java Network 기능은 Exception 처리를 기본으로 해야한다
        try {
            // 3. Java의 URL 객체 생성
            URL obj = new URL(url);
            // 4. URL 객체를 이용해서 접속 시도
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // 5. Web Application의 호출방식 설정
            con.setRequestMethod("GET");
            // 6. 정상적으로 접속되었는지 확인
            // Http Protocol 상태값
            // 200 : 접속성공        404 : not found     500 : internal server error
            // 403 : forbidden      .....
            int responceCode = con.getResponseCode();
            Log.i("BookSearch", "Response Code : " + responceCode);

            // [Error] java.io.IOException: Cleartext HTTP traffic to 70.12.60.91 not permitted
            // 보안설정에 문제가 있는듯...
            // 7. Network 접속을 위해 보안설정
            /*      android 9(Pie) 버전부터는 보안이 강화되었다.
                    기본 Web Protocol이 HTTP >> HTTPS 로 변경되었다.
                    따라서 HTTP protocol을 사용할 경우 특수한 설정이 추가로 필요하다.     */
            /*  --- AndroidMainFest.xml ---   */

            // 8. Server와 연결객체를 이용해 데이터 통로를 만들어야한다.
            /*  서버와의 데이터 통로(Java Stream)을 이용해서 데이터를 읽을 수 있다.
                기본적인 연결통로(InputStream)을 조금 더 효율적인 연결통로로 만들어 사용    */
//            BufferedReader br = new BufferedReader(
//                    new InputStreamReader(con.getInputStream()));
//
//            // 서버가 보내주는 데이터를 읽어 하나의 문자열로 만든다
//            String readLine = "";
//            StringBuffer responseText = new StringBuffer();
//            while ((readLine = br.readLine()) != null) {
//                responseText.append(readLine);
//            }
//            br.close();     // 통로사용 끝, 메모리(resource) 할당 해제

//            String data = "{" + responseText.toString() + "}";

            // 서버에서 얻어온 문자열 확인
//            Log.i("BookSearch", "Message : " + responseText.toString());
            /*Log.i("BookSearch", "data : " + data);*/

            // 가져온 데이터(JSON)를 자료구조화 시켜서 Activity로 반환
            /*  일반적으로 서버쪽 웹 프로그램은 XML이나 JSON으로 결과 데이터를 제공한다
                서버로부터 받은 데이터를 Java의 자료구조로 변환한다                      */
            /*  JSON Parsing Library를 이용해서 쉽고 편하게 JSON을 handling
                가장 대표적인 JSON 처리 Library 중 하나인 JACKSON Library 사용         */

            // JACKSON Library 설치
            // JACKSON Library 사용
            //      > 얻어온 JSON 문자열 데이터를 Java의 String Array로 변환
//            ObjectMapper mapper = new ObjectMapper();
//            String[] resultArr =
//                    mapper.readValue(responseText.toString(), String[].class);
//
//            Log.i("BookSearch", "resultArr : " + resultArr);

//            Book[] resultBook = mapper.readValue(responseText.toString(), Book[].class);
//
//            Log.i("BookSearch", "resultBook : " + resultBook);
//
//            String[] titleArr = new String[resultBook.length];
//            String[] isbnArr = new String[resultBook.length];
//            for (int i=0 ; i<resultBook.length ; i++) {
//                titleArr[i] = resultBook[i].getTitle();
//                isbnArr[i] = resultBook[i].getIsbn();
//            }
//
//            Log.i("BookSearch", "titleArr : " + titleArr);
//            Log.i("BookSearch", "isbnArr : " + isbnArr);

            /*  my style  */
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String readLine = "";
            StringBuffer responseText = new StringBuffer();
            while ((readLine = br.readLine()) != null) {
                responseText.append(readLine);
            }
            br.close();

            Log.i("BookSearch", "responseText : " + responseText.toString());

            String stringText = responseText.toString();
            Log.i("BookSearch", "stringText : " + stringText);

//            String stringText2 = stringText.replace("[", "{");
//            stringText2 = stringText.replace("]", "}");
//
//            Log.i("BookSearch", "stringText2 : " + stringText2);

            ObjectMapper mapper = new ObjectMapper();

            Book[] bookList = mapper.readValue(stringText, Book[].class);
//            Log.i("BookSearch", "bookList : " + bookList);
//            Log.i("BookSearch", "bookList[0] : " + bookList[0].toString());
//            Log.i("BookSearch", "bookList[1] : " + bookList[1].toString());
//            Log.i("BookSearch", "bookList.length : " + bookList.length);
//            Book[] bookList = mapper.readValue(stringText, Book[].class);
//            Log.i("BookSearch", "bookList : " + bookList);

            String[] titleArr = new String[bookList.length];
            String[] isbnArr = new String[bookList.length];
            for (int i=0 ; i<bookList.length ; i++) {
                titleArr[i] = bookList[i].getTitle();
                isbnArr[i]  = bookList[i].getIsbn();
            }

//            Log.i("BookSearch", "titleArr[0] : " + titleArr[0]);
//            Log.i("BookSearch", "isbnArr[11] : " + isbnArr[11]);

            // 결과 데이터를 Activity에 전달
            /*  1. Bundle에 전달할 데이터 저장
            *   2. Message 객체에 Bundle 부착
            *   3. Handler 객체로 Message를 Activity 로 전달   */
            Bundle bundle = new Bundle();

            bundle.putStringArray("BOOKLIST", titleArr);
            bundle.putStringArray("BOOKIDLIST", isbnArr);

            Message msg = new Message();
            msg.setData(bundle);

            handler.sendMessage(msg);

        } catch (Exception e) {
            Log.i("Exception", e.toString());
            //e.printStackTrace();
        }
    }
}