package com.example.timetable.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.timetable.ApiService.WeatherInfoApiService;
import com.example.timetable.R;
import com.example.timetable.datamodel.WeatherInfo;

public class WeatherInfoFragment extends Fragment implements WeatherInfoApiService.OnWeatherInfoReceived {
    public static String TAG = "WeatherInfoFragment";
    private View root;

    private Button requestButton;
    private LinearLayout weatherInfoLayout;
    private View progressBar, noConnection;

    public static WeatherInfoFragment newInstance() {

        Bundle args = new Bundle();
        WeatherInfoFragment fragment = new WeatherInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_weather_info, container, false);
        requestButton = root.findViewById(R.id.request_btn);
        progressBar = root.findViewById(R.id.progressBar);
        noConnection = root.findViewById(R.id.no_connection_img);
        weatherInfoLayout = root.findViewById(R.id.weather_info);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noConnection.setVisibility(View.INVISIBLE);
                weatherInfoLayout.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                WeatherInfoApiService weatherInfoApiService = new WeatherInfoApiService(getContext());
                weatherInfoApiService.getCurrentWeather(WeatherInfoFragment.this);
            }
        });
        return root;
    }

    @Override
    public void onReceived(WeatherInfo weatherInfo) {
        TextView name = root.findViewById(R.id.name_txt);
        name.setText(weatherInfo.getWeatherName());
        TextView description = root.findViewById(R.id.description_txt);
        description.setText(weatherInfo.getWeatherDescription());
        TextView temp = root.findViewById(R.id.temp_txt);
        temp.setText(Double.toString(weatherInfo.getTemp() - 270));
        TextView maxTemp = root.findViewById(R.id.max_temp_txt);
        maxTemp.setText(Double.toString(weatherInfo.getTempMax() - 270));
        TextView minTemp = root.findViewById(R.id.min_temp_txt);
        minTemp.setText(Double.toString(weatherInfo.getTempMin() - 270));
        TextView humidity = root.findViewById(R.id.humidity_txt);
        humidity.setText(Integer.toString(weatherInfo.getHumidity()));
        TextView pressure = root.findViewById(R.id.pressure_txt);
        pressure.setText(Integer.toString(weatherInfo.getPressure()));
        noConnection.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        weatherInfoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError() {
        weatherInfoLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        noConnection.setVisibility(View.VISIBLE);
    }
}
