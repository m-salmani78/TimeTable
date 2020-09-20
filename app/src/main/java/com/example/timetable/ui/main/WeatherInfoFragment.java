package com.example.timetable.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.timetable.ApiService.WeatherInfoApiService;
import com.example.timetable.R;
import com.example.timetable.datamodel.WeatherInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class WeatherInfoFragment extends Fragment implements WeatherInfoApiService.OnWeatherInfoReceived {
    public static String TAG = "WeatherInfoFragment";
    private View root;

    private LinearLayout weatherInfoLayout;
    private View progressBar, noConnection;
    private TextView cityNameText;
    private ConnectivityListener connectivityListener;
    private Snackbar noConnectionSnackBar, connectingSnackBar;

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
        FloatingActionButton requestFab = root.findViewById(R.id.request_btn);
        progressBar = root.findViewById(R.id.progressBar);
        noConnection = root.findViewById(R.id.txt_no_connection);
        weatherInfoLayout = root.findViewById(R.id.weather_info);
        cityNameText = root.findViewById(R.id.txt_city_name);
        requestFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noConnection.setVisibility(View.INVISIBLE);
                weatherInfoLayout.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                WeatherInfoApiService weatherInfoApiService = new WeatherInfoApiService(getContext());
                String str = cityNameText.getText().toString().trim();
                if (str.length() < 2) str = "Yazd";
                weatherInfoApiService.getCurrentWeather(WeatherInfoFragment.this, str, "Ir");
            }
        });

        //initial broadcast receiver
        connectivityListener = new ConnectivityListener();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        Objects.requireNonNull(getContext()).registerReceiver(connectivityListener, intentFilter);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Objects.requireNonNull(getContext()).unregisterReceiver(connectivityListener);
    }

    private class ConnectivityListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            noConnectionSnackBar = Snackbar.make(root, context.getResources().getString(R.string.no_connection), Snackbar.LENGTH_SHORT);
            connectingSnackBar = Snackbar.make(root, context.getResources().getString(R.string.connecting), Snackbar.LENGTH_SHORT);

            if (networkInfo == null) {
//                connectingSnackBar.dismiss();
                noConnectionSnackBar.show();
            } else if (networkInfo.isAvailable()) {
//                noConnectionSnackBar.dismiss();
//                connectingSnackBar.dismiss();
                Snackbar.make(root, context.getResources().getString(R.string.connected), Snackbar.LENGTH_SHORT).show();
            } else {
//                noConnectionSnackBar.dismiss();
                connectingSnackBar.show();
            }
        }
    }
}
