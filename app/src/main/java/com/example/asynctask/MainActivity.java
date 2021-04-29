package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    OkHttpClient client;
    String res;
    Button btnStart;
    myTask myAsyncTask;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button) findViewById(R.id.btnStart);
        tv = findViewById(R.id.textView);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAsyncTask = new myTask();
                myAsyncTask.execute();
            }
        });

    }

    private class myTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                requestHttp("https://ubc.sgp1.cdn.digitaloceanspaces.com/BK_AIR/calib_air_sensor.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray array = new JSONArray(res);
                JSONObject object = array.getJSONObject(2);
                tv.append(object.getString("ID"));
                tv.append(object.getString("PM1_0"));
                tv.append(object.getString("CO"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void requestHttp(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        Response response;

        response = okHttpClient.newCall(request).execute();
        res = response.body().string();

    }
}
