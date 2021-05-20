package com.example.illegalparkingsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.illegalparkingsearch.data.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    EditText inputID;
    EditText inputPW;
    Button joinButton;

    String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.loginButton);
        inputID = (EditText) findViewById(R.id.userInputID);
        inputPW = (EditText) findViewById(R.id.userInputPW);
        joinButton = (Button) findViewById(R.id.join);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = inputID.getText().toString();
                String pw = inputPW.getText().toString();

                HTTPClient.client.create(LoginAPI.class).login(id, pw).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        int code = response.code();
                        String body = response.body();
                        Log.d(TAG, "onResponse: code = " + code + " body = " + body);

                        if (code >= 400) {
                            Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                            try {
                                String error = response.errorBody().string();
                                Log.e(TAG, "onResponse: error = " + error);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "onResponse: success");
                            try {
                                JSONObject jsonBody = new JSONObject(body);
                                if (jsonBody.getBoolean("result")) {
                                    User user = new Gson().fromJson(jsonBody.getString("data"), User.class);
                                    Intent intent = new Intent(getApplicationContext(), structure_.class);
                                    intent.putExtra("user", user);
                                    UserClient.savelUser(MainActivity.this, jsonBody.getString("data"));
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), join.class);
                startActivity(intent);
            }
        });

    }
}

interface LoginAPI {

    @FormUrlEncoded
    @POST("userlogin")
    Call<String> login(
            @Field("id") String id,
            @Field("pw") String pw
    );
}