package com.example.illegalparkingsearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.illegalparkingsearch.data.IllegalInfo;
import com.example.illegalparkingsearch.data.User;
import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Fragment2 extends Fragment {
    private View view;
    private String TAG = this.getClass().getSimpleName();
    CalendarView cctvCalendar;
    MaterialCalendarView calendarView;

    private Context context = null;
    private Gson gson = new Gson();

    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment2, container, false);

        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendatView);

        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();


        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
                //oneDayDecorator
        );

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // 2021-04-26-17:40:25
                String mouth = date.getMonth() + "";
                if (date.getMonth() < 10) {
                    mouth = "0" + mouth;
                }

                String day = date.getDay() + "";
                if (date.getDay() < 10) {
                    day = "0" + day;
                }
                String dateText = date.getYear() + "-" + mouth + "-" + day;
                HTTPClient.client.create(Illegal_API.class).join(dateText).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        int code = response.code();
                        String body = response.body();
                        Log.d(TAG, "onResponse: code = " + code + " body = " + body);

                        if (code >= 400) {
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
                                    JSONArray illegalList = jsonBody.getJSONArray("data");

                                    ArrayList<IllegalInfo> illegalInfos = new ArrayList();
                                    for (int i = 0; i < illegalList.length(); i++) {
                                        IllegalInfo illegalInfo = gson.fromJson(illegalList.getString(i), IllegalInfo.class);
                                        Log.d(TAG, "onResponse: illegalInfo = " + illegalList.getString(i));
                                        illegalInfos.add(illegalInfo);
                                    }

                                    if (illegalInfos.size() > 0) {
                                        DialogActivity dialogActivity = new DialogActivity(context);
                                        dialogActivity.illegalInfos = illegalInfos;
                                        dialogActivity.callFunction();

//                                        DialogActivity.illegalInfos = illegalInfos;
//                                        Intent intent = new Intent(context, DialogActivity.class);
//                                        startActivity(intent);
                                    }


                                } else {
                                    Log.e(TAG, "onResponse: request is failed");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        return view;
    }

}

class SundayDecorator implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();

    public SundayDecorator() {
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SUNDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.RED));
    }
}

class SaturdayDecorator implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();

    public SaturdayDecorator() {
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SATURDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLUE));
    }
}

class OneDayDecorator implements DayViewDecorator {

    private CalendarDay date;

    public OneDayDecorator() {
        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));
        view.addSpan(new ForegroundColorSpan(Color.GREEN));
    }

    /**
     * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
     */
    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}

interface Illegal_API {

    @GET("get_illegal")
    Call<String> join(
            @Query("date") String date
    );
}
