package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    EditText editA;
    myTask myAsyncTask;
    String resp;
    TextView[] res = new TextView[6];
    Button btn_add;
    int A;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editA = findViewById(R.id.input_A);

        btn_add = findViewById(R.id.button_add);
        res[0] = findViewById(R.id.PM1);
        res[1] = findViewById(R.id.PM2);
        res[2] = findViewById(R.id.PM10);
        res[3] = findViewById(R.id.CO2);
        res[4] = findViewById(R.id.CO);
        res[5] = findViewById(R.id.HCHO);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                A = Integer.parseInt(editA.getText() + "");
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
                JSONArray array = new JSONArray(resp);
                JSONObject object = array.getJSONObject((A-1));

                res[0].setText(object.getString("PM1_0"));
                res[1].setText(object.getString("PM2_5"));
                res[2].setText(object.getString("PM10"));
                res[3].setText(object.getString("CO2"));
                res[4].setText(object.getString("CO"));
                res[5].setText(object.getString("HCHO"));

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
        resp = response.body().string();

    }
}
