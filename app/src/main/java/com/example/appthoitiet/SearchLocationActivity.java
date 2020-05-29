package com.example.appthoitiet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appthoitiet.db.WeatherItemDB;
import com.example.appthoitiet.entities.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class SearchLocationActivity extends AppCompatActivity {
    ListView lsvLocation;
    Button btnSearchLocation;
    EditText txtLocation;
    WeatherItemDB weatherItemDB;
    List<String> listLocation = new ArrayList<>();
    String  BASE_URL = "https://api.openweathermap.org/data/2.5/";
    String API_KEY_VALUE = "751d80f6c314139192ffcb62c107e654";
    String  RATE_LIMITER_TYPE = "data";
    String API_KEY_QUERY = "appid";
    String APPLICATION_ID = "plNW8IW0YOIN";
    String SEARCH_API_KEY = "029766644cb160efa51f2a32284310eb";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_location);
        setControl();
        setEvent();
    }

    private void setControl() {
        lsvLocation = findViewById(R.id.lsvLocation);
        btnSearchLocation = findViewById(R.id.btnSearchLocation);
        txtLocation = findViewById(R.id.txtLocation);
    }

    private void setEvent() {
        weatherItemDB = new WeatherItemDB(this);
//        Random generator = new Random();
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
//            weather.setHumidity("80");
//            weatherItemDB.them(weather);
//        }
        listLocation.add("Ha Noi");
        listLocation.add("Ho Chi Minh");
        listLocation.add("Hue");
        listLocation.add("Da Nang");
        listLocation.add("Vung Tau");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listLocation);
        lsvLocation.setAdapter(arrayAdapter);


        lsvLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDataWeatherByLocation(listLocation.get(position));
            }
        });
    }
    public void getDataWeatherByLocation(String location) {
        String url = BASE_URL + "weather?q=" + "HaNoi" + "&appid=" + API_KEY_VALUE;
        RequestQueue requestQueue = Volley.newRequestQueue(SearchLocationActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "OKE", Toast.LENGTH_LONG).show();
                        try {
                            Weather weather = new Weather();
                            JSONObject result = new JSONObject(response);
                            String tenThanhPho = result.getString("name");

                            JSONArray jsonArrayWeather = result.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);

                            String icon = jsonObjectWeather.getString("icon");


                            JSONObject jsonObjectMain = result.getJSONObject("main");
                            String minTemp = String.valueOf(String.format("%.1f", Float.parseFloat(jsonObjectMain.getString("temp_min")) - 273.15) );
                            String maxTemp = String.valueOf(String.format("%.1f", Float.parseFloat(jsonObjectMain.getString("temp_max")) - 273.15));
                            String temp = String.valueOf(String.format("%.1f", Float.parseFloat(jsonObjectMain.getString("temp")) - 273.15));
                            String humidity = jsonObjectMain.getString("humidity");
                            String day = result.getString("dt");
                            long l = Long.parseLong(day);
                            Date date = new Date(l * 1000L);
                            Calendar calendar = Calendar.getInstance();

                            int toDay = calendar.get(Calendar.DAY_OF_WEEK);
                            toDay = toDay -1;
                            if(toDay == 0) {
                                weather.setDay(String.valueOf(7), "INIT");
                                weather.setColor(String.valueOf(getColorOfDay(7)));
                            } else {
                                weather.setDay(String.valueOf(toDay), "INIT");
                                weather.setColor(String.valueOf(getColorOfDay(toDay)));
                            }


                            weather.setNhietDoThapNhat(minTemp);
                            weather.setNhietDoCaoNhat(maxTemp);
                            weather.setNhietDoTrungBinh(temp);
                            SimpleDateFormat formatter = new SimpleDateFormat("hh:MM");
                            String strDate = formatter.format(date);
                            weather.setTime(strDate);
                            weather.setImage(icon);
                            weather.setHumidity(humidity);
                            weatherItemDB.sua(weather,toDay);
                            Intent i = new Intent(SearchLocationActivity.this, MainActivity.class);
                            startActivity(i);

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
    public int getColorOfDay(int day) {
        if(day >=1 && day<=7) {
            return  converColorInt(day);
        } else {
            return Color.parseColor("#28E0AE");
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
}
