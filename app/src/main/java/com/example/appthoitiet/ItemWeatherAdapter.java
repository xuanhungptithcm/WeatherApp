package com.example.appthoitiet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appthoitiet.entities.Weather;

import java.util.List;

public class ItemWeatherAdapter   extends RecyclerView.Adapter<ItemWeatherAdapter.WeatherViewHolder>{
    private List<Weather> weatherList;
    private Activity activity;
    private CardView cardView;
    private ImageView imageViewForecastIcon;
    /**Contructor*/
    public ItemWeatherAdapter(Activity activity,List<Weather> weatherList) {
        this.activity = activity;
        this.weatherList = weatherList;
    }
    /** Create ViewHolder*/
    public class WeatherViewHolder extends  RecyclerView.ViewHolder {
        private TextView tempMax,textViewDayOfWeek, textViewTimeOfDay, textViewTemp, tempMin;
        public WeatherViewHolder(View itemView) {
            super(itemView);
            tempMax =  itemView.findViewById(R.id.tempMax);
            tempMin =  itemView.findViewById(R.id.tempMin);
            textViewDayOfWeek =  itemView.findViewById(R.id.textViewDayOfWeek);
            textViewTimeOfDay = itemView.findViewById(R.id.textViewTimeOfDay);
            textViewTemp = itemView.findViewById(R.id.textViewTemp);
            cardView = itemView.findViewById(R.id.cardWeather);
            imageViewForecastIcon = itemView.findViewById(R.id.imageViewForecastIcon);
        }
    }


    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /** Get layout */
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather,parent,false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        /** Set Value*/
        Weather weather = weatherList.get(position);
        holder.tempMax.setText(weather.getNhietDoCaoNhat());
        holder.tempMin.setText(weather.getNhietDoThapNhat());
        holder.textViewTemp.setText(weather.getNhietDoTrungBinh());
        holder.textViewTimeOfDay.setText(weather.getTime());
        holder.textViewDayOfWeek.setText(weather.getDay());
        cardView.setCardBackgroundColor(Integer.parseInt(weather.getColor()));
        imageViewForecastIcon.setImageResource(R.drawable.a01d_svg);
        /*Sự kiện click vào item*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}
