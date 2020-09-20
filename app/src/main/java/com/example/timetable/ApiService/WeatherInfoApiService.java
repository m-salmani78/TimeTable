package com.example.timetable.ApiService;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.timetable.datamodel.WeatherInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherInfoApiService {
    private static final String TAG = "WeatherInfoApiService";
    private Context context;

    public WeatherInfoApiService(Context context) {
        this.context = context;
    }

    public void getCurrentWeather(final OnWeatherInfoReceived weatherInfoReceived, final String city, String state) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                "http://api.openweathermap.org/data/2.5/weather?q=" + city +
                        "," + state + "&appid=5cd7c03d20e6f4a356a1adad6dcfbbfe",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "onResponse: " + response.toString());
                weatherInfoReceived.onReceived(parseResponseToWeatherInfo(response, city));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.toString());
                weatherInfoReceived.onError();
            }
        });
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 4000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 1;
            }

            @Override
            public void retry(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void getCurrentWeather(OnWeatherInfoReceived weatherInfoReceived) {
        getCurrentWeather(weatherInfoReceived, "Yazd", "Ir");
    }

    private WeatherInfo parseResponseToWeatherInfo(JSONObject response, String city) {
        WeatherInfo weatherInfo = new WeatherInfo(city);
        try {
            JSONArray weatherJsonArray = response.getJSONArray("weather");
            weatherInfo.setWeatherDescription(weatherJsonArray.getJSONObject(0)
                    .getString("description"));
            JSONObject main = response.getJSONObject("main");
            weatherInfo.setTemp(main.getDouble("temp"));
            weatherInfo.setTempMin(main.getDouble("temp_min"));
            weatherInfo.setTempMax(main.getDouble("temp_max"));
            weatherInfo.setPressure(main.getInt("pressure"));
            weatherInfo.setHumidity(main.getInt("humidity"));
            weatherInfo.setWindSpeed(response.getJSONObject("wind").getDouble("speed"));
            Log.i(TAG, "parseResponseToWeatherInfo: successfully parsed");
            return weatherInfo;
        } catch (JSONException e) {
            Log.e(TAG, "parseResponseToWeatherInfo: error");
            return null;
        }
    }

    public interface OnWeatherInfoReceived {
        void onReceived(WeatherInfo weatherInfo);

        void onError();
    }

}
