package com.example.lab4_3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    private TextView tvMessage;
    private Button btnResponse1, btnResponse2, btnAutoResponse;
    private BroadcastReceiver smsReceiver;
    public static boolean isRunning = false;
    private boolean autoResponseEnabled = false;
    private String senderNumber = "";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_SEND_SMS = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},
                    PERMISSION_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_REQUEST_SEND_SMS);
        }

        tvMessage = findViewById(R.id.tvMessage);
        btnResponse1 = findViewById(R.id.btnResponse1);
        btnResponse2 = findViewById(R.id.btnResponse2);
        btnAutoResponse = findViewById(R.id.btnAutoResponse);


        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra(SmsReceiver.SMS_MESSAGE_EXTRA);
                senderNumber = intent.getStringExtra(SmsReceiver.SENDER_EXTRA);
                tvMessage.setText("Message from: " + senderNumber + "\n" + message);
                if (autoResponseEnabled) {
                    sendSms(senderNumber, "I am fine and safe. Worry not!");
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(smsReceiver,
                new IntentFilter(SmsReceiver.SMS_MESSAGE_ACTION));

        // Xử lý dữ liệu từ Intent khi ứng dụng bị tắt
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SmsReceiver.SMS_MESSAGE_EXTRA)) {
            String message = intent.getStringExtra(SmsReceiver.SMS_MESSAGE_EXTRA);
            senderNumber = intent.getStringExtra(SmsReceiver.SENDER_EXTRA);
            tvMessage.setText("Message from: " + senderNumber + "\n" + message);
            if (autoResponseEnabled) {
                sendSms(senderNumber, "I am fine and safe. Worry not!");
            }
        }

        // Thiết lập các nút phản hồi
        btnResponse1.setOnClickListener(v -> {
            Log.d("Number",senderNumber );
            sendSms(senderNumber, "I am fine and safe. Worry not!");
        });

        btnResponse2.setOnClickListener(v -> {
            Log.d("Number",senderNumber );
            sendSms(senderNumber, "Tell my mother I love her");
        });

        btnAutoResponse.setOnClickListener(v -> {
            autoResponseEnabled = !autoResponseEnabled;
            btnAutoResponse.setText(autoResponseEnabled ? "Auto Response: ON" : "Auto Response: OFF");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
    }

    private void sendSms(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(this, "Response sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted to send SMS", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}