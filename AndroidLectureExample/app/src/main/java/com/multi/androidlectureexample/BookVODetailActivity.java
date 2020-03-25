package com.multi.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BookVODetailActivity extends AppCompatActivity {

    private TextView viewTitle;
    private TextView viewAuthor;
    private TextView viewPrice;
    private TextView viewIsbn;

    private ImageView viewImg;
    private Bitmap bitmap;

    private BookVO book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        viewImg = (ImageView) findViewById(R.id.bookDetailImg);
        viewImg.setImageResource(R.drawable.image_not_found);

        viewTitle = (TextView) findViewById(R.id.bookDetailTitle);
        viewAuthor = (TextView) findViewById(R.id.bookDetailAuthor);
        viewPrice = (TextView) findViewById(R.id.bookDetailPrice);
        viewIsbn = (TextView) findViewById(R.id.bookDetailIsbn);

        Intent i = getIntent();
        String keyIsbn = (String) i.getExtras().get("bookIsbn");
        viewIsbn.setText(keyIsbn);

        @SuppressLint("HandlerLeak")
        final Handler dbHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                Bundle bundle = msg.getData();
                book = (BookVO) bundle.getSerializable("bookInfo");

                viewTitle.setText(book.getTitle());
                viewAuthor.setText(book.getAuthor());
                viewPrice.setText(book.getPrice() + " 원");
                viewIsbn.setText(book.getIsbn() + " (ISBN)");

                // by base64
                try {
                    String base64String = book.getImgbase64();
                    String base64Image = base64String.split(",")[1];

                    byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    viewImg.setImageBitmap(bitmap);

                    DisplayMetrics metrics = new DisplayMetrics();
                    WindowManager manager = (WindowManager) getApplicationContext()
                            .getSystemService(Context.WINDOW_SERVICE);
                    manager.getDefaultDisplay().getMetrics(metrics);

                    ViewGroup.LayoutParams param = viewImg.getLayoutParams();

                    viewImg.setMaxHeight((int) (metrics.heightPixels*0.6));

                } catch (Exception e) {
                    Log.i("BookVODetail", "Base64 : "+e);
                }

                // by imgUrl
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            bitmap = getBookImg(book.getImg());
//                        } catch (Exception e) {
//
//                        } finally {
//                            if(bitmap!=null) {
//                                runOnUiThread(new Runnable() {
//                                    @SuppressLint("NewApi")
//                                    public void run() {
//                                        viewImg.setImageBitmap(bitmap);
//
//                                        DisplayMetrics metrics = new DisplayMetrics();
//                                        WindowManager manager = (WindowManager) getApplicationContext()
//                                                .getSystemService(Context.WINDOW_SERVICE);
//                                        manager.getDefaultDisplay().getMetrics(metrics);
//
//                                        ViewGroup.LayoutParams param = viewImg.getLayoutParams();
//
//                                        viewImg.setMaxHeight((int) (metrics.heightPixels*0.5));
//                                    }
//                                });
//                            }
//                        }
//                    }
//                }).start();

            }
        };

        try {
            BookVODetailRunnable runnable = new BookVODetailRunnable(dbHandler, keyIsbn);
            Thread t = new Thread(runnable);
            t.start();
        } catch (Exception e) {
            Log.i("BookDetail", "[Thread] "+ e);
        }


    }

    private Bitmap gitBookImgBase64(String url) {

        return null;
    }

    private Bitmap getBookImg(String url) {

        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        Bitmap retBitmap = null;

        try{
            imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true);    //url로 input받는 flag 허용
            connection.connect();           //연결
            is = connection.getInputStream();   // get inputstream
            retBitmap = BitmapFactory.decodeStream(is);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection!=null) {
                connection.disconnect();
            }
            return retBitmap;
        }

    }
}

class BookVODetailRunnable implements Runnable {

    private Handler handler;
    private String isbn;

    BookVODetailRunnable() {}
    BookVODetailRunnable(Handler handler, String isbn) {
        this.handler = handler;
        this.isbn = isbn;
    }

    @Override
    public void run() {

        String url = "http://70.12.60.91:8080/bookSearchTitle/bookDetail?keyword=" + this.isbn;

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responceCode = con.getResponseCode();
            Log.i("BookDetail", "Response Code : " + responceCode);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String readLine = "";
            StringBuffer responseText = new StringBuffer();
            while ((readLine = br.readLine()) != null) {
                responseText.append(readLine);
            }
            br.close();

            ObjectMapper mapper = new ObjectMapper();
            Bundle bundle = new Bundle();

            BookVO book = mapper.readValue(responseText.toString(), BookVO.class);
            bundle.putSerializable("bookInfo", book);

            Message msg = new Message();
            msg.setData(bundle);

            handler.sendMessage(msg);

        } catch (Exception e) {
            Log.i("BookDetail", "runnable] "+ e);
        }

    }
}
