package com.example.appthoitiet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

public class MainActivity extends AppCompatActivity implements   GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    List<Weather> weatherList = new ArrayList<>();
    RecyclerView recyclerView;
    ItemWeatherAdapter itemWeatherAdapter;
    WeatherItemDB weatherItemDB;
    Button btnSearch;
    ImageView imageViewWeatherIcon;
    private Socket socket;
    private TextView textViewHumidity, textViewTemperature, mTvCurrentLocation;
    SearchLocationActivity searchLocationActivity;
// location

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 5000;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private boolean mIsAutoUpdateLocation;
    private Button mBtnGetLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        try {
            socket = IO.socket("http://192.168.1.5:3000");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
        setControl();
        setEvent();
        Log.d(TAG, "onCreate");
        requestLocationPermissions();

        if (isPlayServicesAvailable()) {
            setUpLocationClientIfNeeded();
            buildLocationRequest();
        } else {
            mTvCurrentLocation.setText("Device does not support Google Play services");
        }
    }

    private void setEvent() {
        weatherItemDB = new WeatherItemDB(this);
        mBtnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGpsOn()) {
                    updateUi();
                } else {
                    Toast.makeText(MainActivity.this, "GPS is OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        layTatCaDuLieu();
    }

    private void setControl() {
        recyclerView = findViewById(R.id.recyclerView);
        itemWeatherAdapter = new ItemWeatherAdapter(this,weatherList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), HORIZONTAL , false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemWeatherAdapter);

        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        imageViewWeatherIcon = findViewById(R.id.imageViewWeatherIcon);
        mTvCurrentLocation = findViewById(R.id.mTvCurrentLocation);
        mBtnGetLocation = findViewById(R.id.mBtnGetLocation);
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





    private void updateUi() {
        if (mLastLocation != null) {
            mTvCurrentLocation.setText(String.format(Locale.getDefault(), "%f, %f",
                    mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            String mode = String.valueOf(mLastLocation.getLatitude()) + "_" + String.valueOf(mLastLocation.getLongitude());
            getDataWeatherByLocation("", mode);
        }
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    requestLocationPermissions();
                }
                break;
            default:
                break;
        }
    }

    private boolean isPlayServicesAvailable() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
                == ConnectionResult.SUCCESS;
    }

    private boolean isGpsOn() {
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void setUpLocationClientIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void buildLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    }

    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation();
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            mLastLocation = lastLocation;
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, String.format(Locale.getDefault(), "onLocationChanged : %f, %f",
                location.getLatitude(), location.getLongitude()));
        mLastLocation = location;
        if (mIsAutoUpdateLocation) {
            updateUi();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    @Override
    public void onDestroy() {
        if (mGoogleApiClient != null
                && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
        Log.d(TAG, "onDestroy LocationService");
        super.onDestroy();
    }

    public void getDataWeatherByLocation(String location, String mode) {
        String url = "";
        String arr[] = mode.split("_");

        url = "https://api.openweathermap.org/data/2.5/" + "weather?lat=" + arr[0].substring(0, 5) + "&lon=" +  arr[1].substring(0, 5) + "&appid=" +"751d80f6c314139192ffcb62c107e654";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
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

                            Toast.makeText(getApplicationContext(), "Lấy Thông Tin Khu Vực " + tenThanhPho + " Thành Công", Toast.LENGTH_LONG).show();
                            JSONObject jsonObjectMain = result.getJSONObject("main");
                            String minTemp = String.valueOf(String.format("%.1f", Float.parseFloat(jsonObjectMain.getString("temp_min")) - 273.15) );
                            String maxTemp = String.valueOf(String.format("%.1f", Float.parseFloat(jsonObjectMain.getString("temp_max")) - 273.15));
                            String temp = String.valueOf(String.format("%.1f", Float.parseFloat(jsonObjectMain.getString("temp")) - 273.15));
                            String humidity = String.valueOf(jsonObjectMain.getInt("humidity"));
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
//                            Intent i = new Intent(SearchLocationActivity.this, MainActivity.class);
//                            i.putExtra("LOCATION", tenThanhPho);
//                            startActivity(i);
                            layTatCaDuLieu();
                            mTvCurrentLocation.setText("Khu vực hiện tại: " + tenThanhPho);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra tên khu vực vừa tìm kiếm", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(stringRequest);
    }
}
