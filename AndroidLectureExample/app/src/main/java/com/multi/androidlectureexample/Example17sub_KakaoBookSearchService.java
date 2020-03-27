package com.multi.androidlectureexample;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class Example17sub_KakaoBookSearchService extends Service {

    private Thread myThread;

    public Example17sub_KakaoBookSearchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String keyword = intent.getExtras().getString("KEYWORD");

//        @SuppressLint("HandlerLeak")
//        final Handler handler = new Handler() {
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//                String[] booklist = msg.getData().getStringArray("BOOKLIST");
//
//                Intent i = new Intent(getApplicationContext(),
//                        Example17_KakaoBookSearchActivity.class);
//                i.putExtra("BOOKLIST", booklist);
//
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//                startActivity(i);
//            }
//        };

        // KakaoBookSearchRunnable runnable = new KakaoBookSearchRunnable(handler, keyword);
        KakaoBookSearchRunnable runnable = new KakaoBookSearchRunnable(keyword);
        myThread = new Thread(runnable);
        myThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myThread.interrupt();
    }

    class KakaoBookSearchRunnable implements Runnable{

//        private Handler handler;
        private String keyword;

        KakaoBookSearchRunnable() {}
        KakaoBookSearchRunnable(String keyword) { this.keyword = keyword; }
//        KakaoBookSearchRunnable(Handler handler, String keyword) {
//            this.handler = handler;
//            this.keyword = keyword;
//        }

        @Override
        public void run() {
            try {
                // Kakao Developer >> Open API 호출방식
                //URL url = new URL("https://dapi.kakao.com/v3/search/book?query="+keyword);

                String url = "https://dapi.kakao.com/v3/search/book?target=title&query=" + keyword;

                // HTTP 접속 -------
                URL obj = new URL(url);
                // URL Connection Open
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                // 연결 설정       // API를 보고 결정한다
                // - 전송방식
                con.setRequestMethod("GET");
                // - header - "Authorization: KakaoAK 8257251b04123102a2155bfa601dbbc5"
                con.setRequestProperty("Authorization", "KakaoAK 8257251b04123102a2155bfa601dbbc5");
                // HTTP 접속 -------

                Log.i("KakaoBook", "Response Code : " + con.getResponseCode());

                // Data 받아오기
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer();

                while ( (line = br.readLine()) != null ) {
                    sb.append(line);
                }

                br.close();

                String jsonData = sb.toString();
                Log.i("KakaoBook", "Response Data : " + jsonData);

                // JSON을 처리해서 documemts라고 되어있는 key값에 대해 Value값을 객체화 해사 가져온다
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map =
                        mapper.readValue(jsonData, new TypeReference<Map<String, Object>>(){});
                Object jsonObject = map.get("documents");
                // [{"authors": ["장삼용"], "title":.....}, {"authors":....}, {...}, ...]
                jsonData = mapper.writeValueAsString(jsonObject);
                Log.i("KakaoBook", "Response Data : " + jsonData);

                ArrayList<KakaoBookVO> booklist =
                        mapper.readValue(jsonData, new TypeReference<ArrayList<KakaoBookVO>>() {});

                ArrayList<String> resultData = new ArrayList<String>();
                for (KakaoBookVO vo : booklist) {
                    resultData.add(vo.getTitle());
                }

                Intent i = new Intent(getApplicationContext(),
                        Example17_KakaoBookSearchActivity.class);
                i.putExtra("BOOKLIST", resultData);

                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(i);


//                int size = booklist.size();
//                String[] titleList = new String[size];
//
//                int i = 0;
//                for (KakaoBookVO vo : booklist) {
//                    titleList[i++] = vo.getTitle();
//                }
//
//                Bundle bundle = new Bundle();
//                bundle.putStringArray("BOOKLIST", titleList);
//                Message msg = new Message();
//                msg.setData(bundle);
//                handler.sendMessage(msg);

            } catch (Exception e) {
                Log.i("KakaoBook", "runnable : " + e.toString());
            }

        }
    }

}

class KakaoBookVO {
    private ArrayList<String> authors;
    private String contents;
    private String datetime;
    private String isbn;
    private String price;
    private String publisher;
    private String sale_price;
    private String status;
    private String thumbnail;
    private String title;
    private ArrayList<String> translators;
    private String url;

    public KakaoBookVO() {
    }

    public KakaoBookVO(ArrayList<String> authors, String contents, String datetime, String isbn, String price, String publisher, String sale_price, String status, String thumbnail, String title, ArrayList<String> translators, String url) {
        this.authors = authors;
        this.contents = contents;
        this.datetime = datetime;
        this.isbn = isbn;
        this.price = price;
        this.publisher = publisher;
        this.sale_price = sale_price;
        this.status = status;
        this.thumbnail = thumbnail;
        this.title = title;
        this.translators = translators;
        this.url = url;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public String getContents() {
        return contents;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPrice() {
        return price;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getSale_price() {
        return sale_price;
    }

    public String getStatus() {
        return status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getTranslators() {
        return translators;
    }

    public String getUrl() {
        return url;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTranslators(ArrayList<String> translators) {
        this.translators = translators;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}