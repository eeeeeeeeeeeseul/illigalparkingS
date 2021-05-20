package com.example.illegalparkingsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class join extends AppCompatActivity {
    Button completeJoin;

    EditText editID;
    EditText editPW;
    EditText editName;
    EditText editLocation;

    String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        completeJoin = (Button) findViewById(R.id.completeJoin);

        editID = findViewById(R.id.join_id);
        editPW = findViewById(R.id.join_pw);
        editName = findViewById(R.id.join_name);
        editLocation = findViewById(R.id.join_location);

        completeJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = editID.getText().toString();
                String pw = editPW.getText().toString();
                String name = editName.getText().toString();
                String location = editLocation.getText().toString();

                HTTPClient.client.create(Join_API.class).join(String.valueOf((int) (Math.random() * 1000000)), name, id, pw, location).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(TAG, "onClick:response.code() " + response.code() + ", response.body() = " + response.body());

                        if (response.code() >= 400) {
                            try {
                                Log.e(TAG, "onClick: response.body() = " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }


}

interface Join_API {

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("usermethod")
    Call<String> join(
            @Field("usernum") String usernum,
            @Field("username") String username,
            @Field("userid") String userid,
            @Field("userpw") String userpw,
            @Field("location") String userLocation
    );
}