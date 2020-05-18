package com.example.timetable.ApiService;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.timetable.AddActivity;
import com.example.timetable.datamodel.AppFeature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RandomPictureApiService {
    private static final String TAG = "RandomPictureApiService";
    private static final String PICTURE_API_URL = "https://picsum.photos/id/1006/600/400";
    Context context;
    private int limit;

    public RandomPictureApiService(Context context) {
        this.context = context;
    }

    public void getPicturesFromServer(final OnPicturesReceived picturesReceived, int page, int limit) {
        if(page<0){
            Toast.makeText(context, "choose the page number", Toast.LENGTH_SHORT).show();
            return;
        }
        this.limit = limit;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                "https://picsum.photos/v2/list?page=" + page + "&limit=" + limit,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, "onPictureResponse: " + response.toString());
                picturesReceived.onReceived(parseResponseToAppFeature(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorPictureResponse: " + error.toString());
                picturesReceived.onError();
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

    public List<AppFeature> parseResponseToAppFeature(JSONArray response) {
        List<AppFeature> appFeatures = new ArrayList<>();
        try {
            for (int i = 0; i < limit; i++) {
                AppFeature appFeature = new AppFeature();
                JSONObject object = response.getJSONObject(i);
                appFeature.setId(object.getInt("id"));
                appFeature.setTitle(object.getString("author"));
                String downloadUrl = object.getString("download_url");
                downloadUrl = downloadUrl.substring(0, downloadUrl.lastIndexOf("/"));
                downloadUrl = downloadUrl.substring(0, downloadUrl.lastIndexOf("/"));
                appFeature.setImageUrl(downloadUrl + "/450/300");
                appFeature.setAuthorPageUrl(object.getString("url"));
                //...
                appFeatures.add(appFeature);
            }
            Log.i(TAG, "parseResponseToPicture: successfully parsed");
            return appFeatures;
        } catch (JSONException e) {
            Log.e(TAG, "parseResponseToPicture: error");
            return null;
        }
    }

    public interface OnPicturesReceived {
        void onReceived(List<AppFeature> appFeatures);

        void onError();
    }
}
