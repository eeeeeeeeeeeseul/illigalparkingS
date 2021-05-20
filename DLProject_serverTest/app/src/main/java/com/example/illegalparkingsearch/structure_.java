package com.example.illegalparkingsearch;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.illegalparkingsearch.data.CCTV;
import com.example.illegalparkingsearch.data.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class structure_ extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; // 바텀네비게이션 뷰
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment4 fragment4;

    TextView userCity;
    TextView userName;
    LinearLayout topLayout;

    private final String TAG = this.getClass().getSimpleName();
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.structure_);

        topLayout = (LinearLayout) findViewById(R.id.topLayout);
        topLayout.bringToFront();
        userCity = (TextView) findViewById(R.id.userCity);
        userName = (TextView) findViewById(R.id.userName);

        User user = UserClient.getUser(this);

        userCity.setText(user.location);
        userName.setText(user.username);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.message:
                        setFrag(0);
                        break;
                    case R.id.database:
                        setFrag(1);
                        break;
                    case R.id.cctv:
                        setFrag(2);
                        break;
                    case R.id.userInfo:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();

        setFrag(0); // 첫화면 설정


    }

    // 프래그먼트 교체가 일어나는 메서드
    private void setFrag(int n) {

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        switch (n) {
            case 0:
                transaction.replace(R.id.main_frame, fragment1);
                transaction.commit();
                break;
            case 1:
                transaction.replace(R.id.main_frame, fragment2);
                transaction.commit();
                break;
            case 2:
                transaction.replace(R.id.main_frame, fragment3);
                transaction.commit();
                break;
            case 3:
                transaction.replace(R.id.main_frame, fragment4);
                transaction.commit();
                break;
        }
    }
}
