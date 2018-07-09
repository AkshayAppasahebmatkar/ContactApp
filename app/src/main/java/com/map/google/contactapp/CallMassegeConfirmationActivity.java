package com.map.google.contactapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by owner on 28/05/2018.
 */

public class CallMassegeConfirmationActivity extends Activity {

    Context context;
    Button call_btn, message_btn;
    LinearLayout bounces_layout;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dilog_call_massege_confirmation);

        Bundle bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("number");

        context = getApplicationContext();
        bounces_layout = (LinearLayout) findViewById(R.id.bounces_layout);
        call_btn = (Button) findViewById(R.id.call_btn);
        message_btn = (Button) findViewById(R.id.massege_btn);

        @SuppressLint("ResourceType")
        Animation animMoveToTop = AnimationUtils.loadAnimation(context, R.animator.slow_bounce);
        bounces_layout.startAnimation(animMoveToTop);
        
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+ phoneNumber));
                if (ActivityCompat.checkSelfPermission(CallMassegeConfirmationActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
                finish();
            }
        });
        message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageToSend = "This is a message for testing purposes";
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(phoneNumber, null, messageToSend, null,null);
                //SmsManager.getDefault().sendTextMessage(phoneNumber, null, messageToSend, null,null);
                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+phoneNumber));
                smsIntent.putExtra("sms_body",messageToSend);
                startActivity(smsIntent);
                finish();
            }
        });
    }
}