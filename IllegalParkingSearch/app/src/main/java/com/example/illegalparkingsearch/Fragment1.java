package com.example.illegalparkingsearch;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.TransactionTooLargeException;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.klinker.android.logger.Log;

import org.w3c.dom.Text;

import static com.android.mms.logs.LogTag.TAG;

public class Fragment1 extends Fragment {
    private View view;
    Button messageButton;
    LinearLayout layout;
    TextView carnumText;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Context mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment1, container, false);
        messageButton = (Button) view.findViewById(R.id.sendMessageButton1);
        layout = (LinearLayout) view.findViewById(R.id.messageLayout);
        carnumText = (TextView) view.findViewById(R.id.carnumberText1);

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   String phoneNumber = "010-9455-9547";
                String message_ = "Test";
                String attachUrl ="@drawable/testimage.jpg";

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setClassName("com.android.mms","com.android.mms.ui.ComposeMessageActivity");
                sendIntent.putExtra("address",phoneNumber);
                sendIntent.putExtra("sms_body",message_);
               sendIntent.putExtra(Intent.EXTRA_STREAM,attachUrl);
                sendIntent.setType("image/jpg");
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(sendIntent);*/
            }
        });
        return view;
    }

    private void sendSMS(String phoneNumber, String Message){
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(getContext(), 0,new Intent(SENT),0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getContext(),0,new Intent(DELIVERED),0);

        /*registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        },new IntentFilter(SENT));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber,null,Message,sentPI,deliveredPI);*/
    }

}
