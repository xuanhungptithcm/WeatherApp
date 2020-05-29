package com.example.appthoitiet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appthoitiet.db.WeatherItemDB;
import com.example.appthoitiet.entities.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

public class MainActivity extends AppCompatActivity {
    List<Weather> weatherList = new ArrayList<>();
    RecyclerView recyclerView;
    ItemWeatherAdapter itemWeatherAdapter;
    WeatherItemDB weatherItemDB;
    Button btnSearch;
    private TextView textViewWeatherMain;
    String  BASE_URL = "https://api.openweathermap.org/data/2.5/";
    String API_KEY_VALUE = "751d80f6c314139192ffcb62c107e654";
    String  RATE_LIMITER_TYPE = "data";
    String API_KEY_QUERY = "appid";
    String APPLICATION_ID = "plNW8IW0YOIN";
    String SEARCH_API_KEY = "029766644cb160efa51f2a32284310eb";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        setControl();
        setEvent();
    }

    private void setEvent() {
        Random generator = new Random();
        weatherItemDB = new WeatherItemDB(this);
//        for(int i=1;i<=7;i++) {
//            Weather weather = new Weather();
//            weather.setDay(String.valueOf(i), "INIT");
//            weather.setColor(String.valueOf(getColorOfDay(i)));
//            weather.setNhietDoThapNhat(String.valueOf(generator.nextInt(21) + 20));
//            weather.setNhietDoCaoNhat(String.valueOf(generator.nextInt(21) + 20));
//            weather.setNhietDoTrungBinh(String.valueOf(generator.nextInt(21) + 20));
//            Date date = new Date();
//            SimpleDateFormat formatter = new SimpleDateFormat("hh:MM");
//            String strDate = formatter.format(date);
//            weather.setTime(strDate);
//            weather.setImage("00:00");
//            weatherItemDB.them(weather);
//        }
        layTatCaDuLieu();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataWeatherByLocation("");
            }
        });
    }

    private void setControl() {
        recyclerView = findViewById(R.id.recyclerView);
        itemWeatherAdapter = new ItemWeatherAdapter(this,weatherList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), HORIZONTAL , false);
//        StaggeredGridLayoutManager gridLayoutManager =
//                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemWeatherAdapter);

        textViewWeatherMain = findViewById(R.id.textViewWeatherMain);
        btnSearch = findViewById(R.id.btnSearch);
    }
    public int getColorOfDay(int day) {
        if(day >=1 && day<=7) {
        return  converColorInt(day);
        } else {
            return Color.parseColor("#28E0AE");
        }
    }
    public void layTatCaDuLieu() {
        Cursor cursor = weatherItemDB.layTatCaDuLieu();
        if(cursor != null) {
            weatherList.clear();
            while (cursor.moveToNext()) {
                Weather hd = new Weather();
                hd.setId(String.valueOf(cursor.getInt(0)));
                hd.setDay(cursor.getString(1), "");
                hd.setTime(cursor.getString(2));
                hd.setNhietDoCaoNhat(cursor.getString(3));
                hd.setNhietDoThapNhat(cursor.getString(4));
                hd.setNhietDoTrungBinh(cursor.getString(5));
                hd.setColor(cursor.getString(6));
                hd.setImage(cursor.getString(7));
                weatherList.add(hd);
            }
            itemWeatherAdapter.notifyDataSetChanged();
        }
    }

    public int converColorInt( int day) {
        switch (day) {
            case 1: {
                return Color.parseColor("#28E0AE");
            }
            case 2: {
                return Color.parseColor("#FF0090");
            }
            case 3: {
                return  Color.parseColor("#FFAE00");
            }
            case 4: {
                return Color.parseColor("#0090FF");
            }
            case 5: {
                return Color.parseColor("#DC0000");
            }
            case 6: {
                return Color.parseColor("#0051FF");
            }
            case 7: {
                return Color.parseColor("#3D28E0");
            }
        }
        return Color.parseColor("#28E0AE");
    }
    public void getDataWeatherByLocation(String location) {
        String url = BASE_URL + "weather?q=" + "HaNoi" + "&appid=" + API_KEY_VALUE;
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "OKE", Toast.LENGTH_LONG).show();
                        try {
                            JSONObject result = new JSONObject(response);
                            String tenThanhPho = result.getString("name");
                            textViewWeatherMain.setText(tenThanhPho);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Food source is not responding (USDA API)", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(stringRequest);
    }
}
