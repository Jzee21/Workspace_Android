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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class BookDetailActivity extends AppCompatActivity {

    private TextView viewTitle;
    private TextView viewAuthor;
    private TextView viewPrice;
    private TextView viewIsbn;

    private ImageView viewImg;
    private Bitmap bitmap;

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

        String keyTitle = (String) i.getExtras().get("bookTitle");
        String keyIsbn = (String) i.getExtras().get("bookIsbn");
        viewTitle.setText(keyTitle);
        viewIsbn.setText(keyIsbn);

        @SuppressLint("HandlerLeak")
        final Handler dbHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                Bundle bundle = msg.getData();
                // Book(String isbn, String img, String title, String author, String price)
                final Book book = new Book(bundle.getString("isbn"),
                            bundle.getString("img"),
                            bundle.getString("title"),
                            bundle.getString("author"),
                            bundle.getString("price"));
                // Log.i("BookDetail", "handler] "+ book.toString());

                viewTitle.setText(book.getTitle());
                viewAuthor.setText(book.getAuthor());
                viewPrice.setText(book.getPrice() + " 원");
                viewIsbn.setText(book.getIsbn() + " (ISBN)");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bitmap = getBookImg(book.getImg());
                        } catch (Exception e) {

                        } finally {
                            if(bitmap!=null) {
                                runOnUiThread(new Runnable() {
                                    @SuppressLint("NewApi")
                                    public void run() {
                                        viewImg.setImageBitmap(bitmap);

                                        DisplayMetrics metrics = new DisplayMetrics();
                                        WindowManager manager = (WindowManager) getApplicationContext()
                                                .getSystemService(Context.WINDOW_SERVICE);
                                        manager.getDefaultDisplay().getMetrics(metrics);

                                        ViewGroup.LayoutParams param = viewImg.getLayoutParams();

                                        viewImg.setMaxHeight((int) (metrics.heightPixels*0.5));
                                    }
                                });
                            }
                        }
                    }
                }).start();

//                try {
//                    URL imgUrl = new URL(book.getImg());
//                    URLConnection conn = imgUrl.openConnection();
//                    conn.connect();
//                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
//                    bitmap = BitmapFactory.decodeStream(bis);
//                    bis.close();
//                    viewImg.setImageBitmap(bitmap);
//                } catch (Exception e) {
//                    Log.i("BookDetail", "Image] "+ e);
//                }
                // Image] android.os.NetworkOnMainThreadException

            }
        };

        try {
            BookDetailRunnable runnable = new BookDetailRunnable(dbHandler, keyIsbn);
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

class BookDetailRunnable implements Runnable {

    private Handler handler;
    private String isbn;

    BookDetailRunnable() {}
    BookDetailRunnable(Handler handler, String isbn) {
        this.handler = handler;
        this.isbn = isbn;
    }

    @Override
    public void run() {

        String url = "http://70.12.60.91:8080/bookSearch/searchDatail?keyword=" + this.isbn;

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

            // Log.i("BookDetail", "Book] "+ responseText.toString());
            /*[
                {"isbn":"89-7914-397-4",
                 "img":"http://image.hanbit.co.kr/cover/_m_1397m.gif",
                 "title":"뇌를 자극하는 Java 프로그래밍",
                 "author":"김윤명",
                 "price":"27000"
                }
              ] */

            ObjectMapper mapper = new ObjectMapper();
            Bundle bundle = new Bundle();

            Book[] book = mapper.readValue(responseText.toString(), Book[].class);
            //
//            Log.i("BookDetail", "Book] "+ book.length);
//            Log.i("BookDetail", "Book] "+ book[0].toString());

//            bundle.putString("String key", "String value");
//            bundle.putStringArray("String key", new String[]{"String[] values"});
            bundle.putString("title", book[0].getTitle());
            bundle.putString("isbn", book[0].getIsbn());
            bundle.putString("img", book[0].getImg());
            bundle.putString("author", book[0].getAuthor());
            bundle.putString("price", book[0].getPrice());

            Message msg = new Message();
            msg.setData(bundle);

            handler.sendMessage(msg);

        } catch (Exception e) {
            Log.i("BookDetail", "runnable] "+ e);
        }

    }
}