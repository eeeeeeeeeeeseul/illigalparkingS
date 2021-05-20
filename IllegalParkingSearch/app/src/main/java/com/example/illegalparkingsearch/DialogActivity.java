package com.example.illegalparkingsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.illegalparkingsearch.data.IllegalInfo;

import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity {

    public static ArrayList<IllegalInfo> illegalInfos;

    private RecyclerView illegalListView;
    private IllegalAdapter illegalAdapter;


    private Context context;
    Intent intent;

//    public DialogActivity(Context context, Intent intent) {
//        this.context = context;
//        this.intent = intent;
//    }

    public DialogActivity(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        Log.e("다이얼로그함수로이동", "");

        /**
         * 인텐트
         */
//        try {
//            //  intent = getIntent(); /*데이터 수신*/
//            illegalInfos = intent.getExtras().getString("illegalInfos");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        if (illegalInfos == null) {
            // 커스텀 다이얼로그를 종료한다.
            dlg.dismiss();
            return;
        }

        illegalListView = dlg.findViewById(R.id.illegal_list);
        illegalListView.setLayoutManager(new LinearLayoutManager(this));
        illegalAdapter = new IllegalAdapter();
        illegalListView.setAdapter(illegalAdapter);
    }

}


class IllegalAdapter extends RecyclerView.Adapter<IllegalView> {

    private String TAG = this.getClass().getSimpleName();

    @NonNull
    @Override
    public IllegalView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_illegal, parent, false);
        return new IllegalView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IllegalView holder, int position) {
        IllegalInfo illegalInfo = DialogActivity.illegalInfos.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
            }
        });

        Log.d(TAG, "onBindViewHolder: illegalInfo = " + illegalInfo.carnum);
        Log.d(TAG, "onBindViewHolder: illegalInfo = " + illegalInfo.carnum);
        Log.d(TAG, "onBindViewHolder: illegalInfo = " + illegalInfo.carnum);

        holder.carNum.setText(illegalInfo.carnum);
        holder.time.setText(illegalInfo.illigal_time);
        holder.cctv.setText(illegalInfo.cctvname);
    }

    @Override
    public int getItemCount() {
        return DialogActivity.illegalInfos.size();
    }
}

class IllegalView extends RecyclerView.ViewHolder {

    TextView time;
    TextView carNum;
    TextView cctv;

    public IllegalView(@NonNull View itemView) {
        super(itemView);

        time = itemView.findViewById(R.id.time);
        carNum = itemView.findViewById(R.id.car_num);
        cctv = itemView.findViewById(R.id.cctv_name);
    }
}