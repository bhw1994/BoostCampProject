package com.example.bhw.boostcamp;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<MovieInfo> list;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyAdapter myAdapter;
    private boolean syncToken=true;
    private String keyword;
    private int dataIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        list=new ArrayList<>();


        myAdapter = new MyAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(myAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(-1)) {
                    Log.i("result", "Top of list");
                } else if (!recyclerView.canScrollVertically(1)) {
                    Log.i("result", "End of list");
                    search(keyword,dataIndex);
                } else {
                    Log.i("result", "idle");
                }
            }
        });



        Button searchButton=findViewById(R.id.search_button);
        final EditText searchKeyword=findViewById(R.id.search_keyword);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                dataIndex=0;
                keyword=searchKeyword.getText().toString();
                search(keyword,dataIndex);
            }
        });




    }
    private void search(final String keyword, final int startIndex){

        if(syncToken==false)
            return;

        final Handler handler=new Handler();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    syncToken=false;
                    URL Url = new URL("https://openapi.naver.com/v1/search/movie.json?start="+(startIndex+1)+"&display=10&query="+keyword); // URL화 한다.
                    //URL Url = new URL("https://openapi.naver.com/v1/search/movie.xml?query=%EC%A3%BC%EC%8B%9D&display=10&start=1&genre=1"); // URL화 한다.

                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
                    conn.setRequestMethod("GET"); // get방식 통신

                    // Request Header값 셋팅 setRequestProperty(String key, String value)
                    conn.setRequestProperty("X-Naver-Client-Id", "EJolayZxhXKXeuNKUu1o");
                    conn.setRequestProperty("X-Naver-Client-Secret", "kwhS067pF5");




                    //strCookie = conn.getHeaderField("Set-Cookie"); //쿠키데이터 보관

                    int statusCode = conn.getResponseCode();
                    Log.d("result","statusCode : "+statusCode);
                    //InputStream is = conn.getInputStream(); //input스트림 개방
                    InputStream is = null;

                    if (statusCode >= 200 && statusCode < 400) {
                        // Create an InputStream in order to extract the response object
                        is = conn.getInputStream();
                    }
                    else {
                        is = conn.getErrorStream();
                    }


                    StringBuilder builder = new StringBuilder(); //문자열을 담기 위한 객체
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8")); //문자열 셋 세팅
                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line+ "\n");
                    }

                    String result = builder.toString();
                    Log.d("result",result);


                    JSONObject jsonObject=new JSONObject(result);

                    Log.d("result",jsonObject.get("lastBuildDate").toString());


                    JSONArray jsonArray=jsonObject.getJSONArray("items");
                    JSONObject tmp;
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        tmp=jsonArray.getJSONObject(i);
                        list.add(new MovieInfo(tmp));
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.notifyDataSetChanged();
                            //myAdapter.notify();
                            dataIndex+=10;
                        }
                    });

                }
                catch (Exception e)
                {

                    Log.d("result","fail");
                    Log.d("result",e.toString());

                }
                finally {
                    syncToken=true;
                }
            }
        });
        thread.start();

    }
}
