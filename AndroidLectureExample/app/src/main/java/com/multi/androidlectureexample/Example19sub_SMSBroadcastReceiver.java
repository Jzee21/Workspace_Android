package com.multi.androidlectureexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Date;

/*
*   보안설정이 잘 되어 있으면
*   특정 signal(Broadcast 가 전파되면)이 발생하면
*   해당 Broadcast 를 받을 수 있다.
*/
public class Example19sub_SMSBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
        */
        Log.i("SMSTest", "SMS Received");

        // Intent 안의 정보 추출
        Bundle bundle = intent.getExtras();

        // Bundle 객체 안에는 Key, Value 형태의 여러가지 데이터가 저장되어있다.
        // SMS 정보는 "pdus" 라는 Key 값으로 저장되어있다.
        // 짧은 시간에(동시에 가까운 시간에) 여러개의 SMS가 도착할 수 있다.
        // 객체 1개가 SMS 1건을 의미한다.
        Object[] obj = (Object[]) bundle.get("pdus");

        // 여기서는 1개의 SMS만 수신한다고 가정한다.
        // convert Object to SmsMessage
        SmsMessage[] message = new SmsMessage[obj.length];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            message[0] = SmsMessage.createFromPdu((byte[]) obj[0], format);
        } else {
            message[0] = SmsMessage.createFromPdu((byte[]) obj[0]);
        }

        String sender = message[0].getOriginatingAddress();     // 발신자 전화번호
        String msg = message[0].getMessageBody();           // SMS 내용
        String reDate = new Date(message[0].getTimestampMillis()).toString();

//        Log.i("SMSTest", "Sender : " + sender);
//        Log.i("SMSTest", "Message : " + msg);
//        Log.i("SMSTest", "Time : " + reDate);

        Intent i = new Intent(context, Example19_BRSMSActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        i.putExtra("sender", sender);
        i.putExtra("msg", msg);
        i.putExtra("date", reDate);

        context.startActivity(i);

    }
}
