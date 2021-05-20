package com.example.illegalparkingsearch;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.illegalparkingsearch.data.CCTV;
import com.example.illegalparkingsearch.data.IllegalInfo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Fragment3 extends Fragment {
    private View view;

    private RecyclerView cctvListView;
    private CctvListAdapter cctvListAdapter;
    private LinearLayoutManager cctvLayoutManager;

    private final String TAG = this.getClass().getSimpleName();
    private Gson gson = new Gson();

    private ArrayList<CCTV> cctvArrayList = new ArrayList<>();
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cctvLayoutManager = new LinearLayoutManager(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment3, container, false);

        cctvListView = view.findViewById(R.id.cctv_list);
        cctvListAdapter = new CctvListAdapter();

        cctvListView.setLayoutManager(cctvLayoutManager);
        cctvListView.setAdapter(cctvListAdapter);

        HTTPClient.client.create(CCTV_API.class).getCCTV(UserClient.getUser(context).location).enqueue(new Callback<String>() {
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
                            JSONArray cctvList = jsonBody.getJSONArray("data");

                            cctvArrayList = new ArrayList<>();

                            for (int i = 0; i < cctvList.length(); i++) {
                                CCTV cctv = gson.fromJson(cctvList.getString(i), CCTV.class);
                                Log.d(TAG, "onResponse: CCTV = " + cctvList.getString(i));
                                cctvArrayList.add(cctv);
                            }

                            cctvListAdapter.changeList(cctvArrayList);
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

            }
        });

        return view;
    }
}

class CctvListAdapter extends RecyclerView.Adapter<CCTVViewHolder> {

    ArrayList<CCTV> cctvArrayList = new ArrayList<>();

    public void changeList(ArrayList<CCTV> cctvArrayList) {
        this.cctvArrayList = cctvArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CCTVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cctv, parent, false);
        return new CCTVViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CCTVViewHolder holder, int position) {
        holder.location.setText(cctvArrayList.get(position).location);
        holder.name.setText(cctvArrayList.get(position).cctvname);
    }

    @Override
    public int getItemCount() {
        return cctvArrayList.size();
    }
}

class CCTVViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView location;

    public CCTVViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.cctv_name);
        location = itemView.findViewById(R.id.cctv_location);
    }
}

interface CCTV_API {

    @GET("search_cctv")
    Call<String> getCCTV(@Query("location") String location);

}
