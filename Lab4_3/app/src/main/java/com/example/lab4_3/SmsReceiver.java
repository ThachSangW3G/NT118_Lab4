package com.example.lab4_3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsReceiver";
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String SMS_MESSAGE_ACTION = "com.example.emergencyresponse.SMS_MESSAGE_ACTION";
    public static final String SMS_MESSAGE_EXTRA = "sms_message";
    public static final String SENDER_EXTRA = "sender";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] messages = null;
            String strMessage = "";
            String sender = "";

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                messages = new SmsMessage[pdus.length];

                for (int i = 0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sender = messages[i].getOriginatingAddress();
                    strMessage += messages[i].getMessageBody();
                }

                // Kiểm tra tin nhắn có chứa từ khóa "are you ok?"
                if (strMessage.toLowerCase().contains("are you ok?")) {
                    // Kiểm tra xem MainActivity có đang chạy không
                    if (MainActivity.isRunning) {
                        // MainActivity đang chạy, broadcast nội dung nhận được
                        Intent localIntent = new Intent(SMS_MESSAGE_ACTION);
                        localIntent.putExtra(SMS_MESSAGE_EXTRA, strMessage);
                        localIntent.putExtra(SENDER_EXTRA, sender);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
                    } else {
                        // MainActivity không chạy, khởi động MainActivity
                        Log.d("Sanggg", "Start activity");
                        Intent startIntent = new Intent(context, MainActivity.class);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startIntent.putExtra(SMS_MESSAGE_EXTRA, strMessage);
                        startIntent.putExtra(SENDER_EXTRA, sender);
                        context.startActivity(startIntent);
                    }
                }
            }
        }
    }
}
