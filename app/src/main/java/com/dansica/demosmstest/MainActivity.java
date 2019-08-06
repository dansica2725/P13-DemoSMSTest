package com.dansica.demosmstest;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;

import com.dansica.smstest.R;

public class MainActivity extends AppCompatActivity {

    Button sendBtn;
    SmsManager smsManager;
    BroadcastReceiver messageReceiver = new MessageReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendBtn = findViewById(R.id.sendBtn);

        checkPermission();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        MainActivity.this.registerReceiver(messageReceiver, intentFilter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("5556", null, "Hello World", null, null);
            }
        });
    }

    public void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int permissionReceiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED && permissionReceiveSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{
                    Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};

            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(messageReceiver);
    }
}
