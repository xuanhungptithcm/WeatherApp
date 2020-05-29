package com.example.appthoitiet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

public class MainActivity extends AppCompatActivity {
    List<Weather> weatherList = new ArrayList<>();
    RecyclerView recyclerView;
    ItemWeatherAdapter itemWeatherAdapter;
    WeatherItemDB weatherItemDB;
    Button btnSearch;
    ImageView imageViewWeatherIcon;
    private TextView textViewHumidity, textViewTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        setControl();
        setEvent();
    }

    private void setEvent() {
     //   Random generator = new Random();
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
//            weather.setHumidity("80");
//            weatherItemDB.them(weather);
//        }
        layTatCaDuLieu();
//        textViewWeatherMain.setText("HANOI");

//        textViewWeatherMain.setText(getIntent().getExtras().getString("LOCATION"));
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

        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewTemperature = findViewById(R.id.textViewTemperature);
       imageViewWeatherIcon = findViewById(R.id.imageViewWeatherIcon);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String result = data.getStringExtra("LOCATION");
        // Sử dụng kết quả result bằng cách hiện Toast
        Toast.makeText(this, "Result: " + result, Toast.LENGTH_LONG).show();
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
                hd.setHumidity(cursor.getString(8));
                weatherList.add(hd);
            }
            Calendar calendar = Calendar.getInstance();
            int toDay = calendar.get(Calendar.DAY_OF_WEEK);
            toDay = toDay - 1;
            if(toDay == 0) {
                textViewTemperature.setText(weatherList.get(6).getNhietDoTrungBinh() + "°");
                textViewHumidity.setText(weatherList.get(6).getHumidity() + "%");
                String name = "a" + weatherList.get(toDay - 1).getImage() + "_svg";
                int resID = getResources().getIdentifier( name, "drawable" , getPackageName()) ;
                imageViewWeatherIcon.setImageResource(resID);
            } else {
                textViewTemperature.setText(weatherList.get(toDay - 1).getNhietDoTrungBinh() + "°");
                String name = "a" + weatherList.get(toDay - 1).getImage() + "_svg";
                int resID = getResources().getIdentifier( name, "drawable" , getPackageName()) ;
                imageViewWeatherIcon.setImageResource(resID);
                textViewHumidity.setText(weatherList.get(toDay - 1).getHumidity() + "%");


            }
            itemWeatherAdapter.notifyDataSetChanged();
        }
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
