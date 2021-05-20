package com.example.illegalparkingsearch;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HTTPClient {

    public static String BASE_URL = "http://10.0.2.2:5000/";

    public static Retrofit client = new Retrofit.Builder().baseUrl(BASE_URL) //addConverterFactory : 통신이 완료된 후 어떤 Converter를 이용하여 데이터를 파싱할 것인지에 대한 설정
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(new Gson())) //retrofit 응답 Gson 처리
            .build();

}
