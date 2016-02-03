package com.example.vasista.otp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("sms","Recieved");
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Bundle intent_data = intent.getExtras();
        if (intent_data != null){
            Object[] sms = (Object[]) intent_data.get("pdus");
            for (int i = 0; i < sms.length; i++){

                SmsMessage smsMessage = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], "3gpp");
                }else{
                    smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                }


                if(!smsMessage.getOriginatingAddress().equalsIgnoreCase(context.getString(R.string.otp_originate))){
                    return;
                }

                Log.v("sms", "" + smsMessage.getOriginatingAddress() + " <> "+smsMessage.getMessageBody());

                Pattern p = Pattern.compile("(|^)\\d{6}");

                Matcher m = p.matcher(smsMessage.getMessageBody());

                if (m.find()){
                    Log.v("sms", "match" + m.group(0));
                    MainActivity mainActivity = new MainActivity();
                    mainActivity.updateOtp(m.group(0));
                }else{
                    Log.v("sms","not match");
                }

            }
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
