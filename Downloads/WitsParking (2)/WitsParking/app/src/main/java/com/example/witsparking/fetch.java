package com.example.witsparking;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class fetch {
    public void getdata(Activity a, String method,requesthandler r){
        OkHttpClient client = new OkHttpClient();
        Request request=new Request.Builder().url(method).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                 final String responsedata=response.body().string();
                 a.runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         r.processresponse(responsedata);

                     }
                 });
            }
        });

    }
}
