package com.example.lab4_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_MESSAGE_ACTION = "com.example.smsreceiver.SMS_MESSAGE_ACTION";
    public static final String SMS_MESSAGE_EXTRA = "sms_message";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] messages = null;
            String strMessage = "";

            if (bundle != null) {
                // Lấy các tin nhắn SMS
                Object[] pdus = (Object[]) bundle.get("pdus");
                messages = new SmsMessage[pdus.length];

                for (int i = 0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    strMessage += "SMS từ: " + messages[i].getOriginatingAddress();
                    strMessage += " : ";
                    strMessage += messages[i].getMessageBody().toString();
                    strMessage += "\n";
                }

                // Hiển thị tin nhắn SMS nhận được
                Toast.makeText(context, "Hey! You have a new message!", Toast.LENGTH_LONG).show();
                Intent localIntent = new Intent(SMS_MESSAGE_ACTION);
                localIntent.putExtra(SMS_MESSAGE_EXTRA, strMessage);
                LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
            }
        }
    }
}
