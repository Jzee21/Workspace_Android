package com.multi.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*  - 첫 화면은 키워드를 이용한 책 목록 출력
    - 특정 책 터치 >>> 해당 책의 세부내용을 새로운 Activity에서 출력
 */
public class Example13_BookSearchDetailActivity extends AppCompatActivity {

    private Button detailSearchBtn;
    private EditText detailSearchTitle;
    private ListView detailSearchList;

    private BookVO[] bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example13_book_search_detail);

        detailSearchBtn = (Button)findViewById(R.id.detailSearchBtn);
        detailSearchTitle = (EditText)findViewById(R.id.detailSearchTitle);
        detailSearchList = (ListView)findViewById(R.id.detailSearchList);

        @SuppressLint("HandlerLeak")
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                Bundle bundle = msg.getData();
                bookList = (BookVO[]) bundle.getSerializable("BOOKLIST");

                String[] titles = new String[bookList.length];
                int index = 0;
                for (BookVO vo : bookList) {
                    titles[index++] = vo.getTitle();
                }

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_list_item_1, titles);
                detailSearchList.setAdapter(adapter);

            }
        };

        // keyword로 검색
        detailSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = detailSearchTitle.getText().toString();

                MyBookInfoRunnable runnable = new MyBookInfoRunnable(handler, keyword);
                Thread t = new Thread(runnable);
                t.start();
            }
        });

        // BookDetailInfo Activity
        detailSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), bookList[position].getIsbn(), Toast.LENGTH_SHORT).show();

                // Intent i = new Intent();
            }
        });

    }
}

class MyBookInfoRunnable implements Runnable {

    private Handler handler;
    private String keyword;

    // Constructor Injection
    MyBookInfoRunnable() {}
    MyBookInfoRunnable(Handler handler, String keyword) {
        this.handler = handler;
        this.keyword = keyword;
    }

    @Override
    public void run() {
        try {
            // Conn Web App
            URL url = new URL("http://70.12.60.91:8080/bookSearchTitle/bookinfo?keyword=" + keyword);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responceCode = con.getResponseCode();
            Log.i("BookSearchDetail", "Response Code : " + responceCode);

            // get JSON from Web App
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String readLine = "";
            StringBuffer responseText = new StringBuffer();
            while ((readLine = br.readLine()) != null) {
                responseText.append(readLine);
            }
            br.close();

            String jsonData = responseText.toString();

            Log.i("BookSearchDetail", "jsonData : " + jsonData);

            // JSON to Java Object
            ObjectMapper mapper = new ObjectMapper();
//            Book[] bookList = mapper.readValue(stringText, Book[].class);
            BookVO[] resultArr = mapper.readValue(jsonData, BookVO[].class);

            // Handler.sendMessage()
            Bundle bundle = new Bundle();
            bundle.putSerializable("BOOKLIST", resultArr);

            Message msg = new Message();
            msg.setData(bundle);

            handler.sendMessage(msg);


        } catch (Exception e) {
            Log.i("BookSearchDetail", "Runnable : ");
        }
    }
}

class BookVO {

    // field
    private String isbn;
    private String title;
    private String date;
    private int page;
    private int price;
    private String author;
    private String translator;
    private String supplement;
    private String publisher;
    private String imgurl;
    private String imbase64;

    // constructor
    public BookVO() {}


    // private field method
    // getter
    // setter
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getSupplement() {
        return supplement;
    }

    public void setSupplement(String supplement) {
        this.supplement = supplement;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImbase64() {
        return imbase64;
    }

    public void setImbase64(String imbase64) {
        this.imbase64 = imbase64;
    }

}