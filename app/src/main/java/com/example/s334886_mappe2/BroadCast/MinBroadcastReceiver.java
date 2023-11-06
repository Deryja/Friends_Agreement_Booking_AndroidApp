package com.example.s334886_mappe2.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

//MinBroadcastreciever --> Periodisk --> MinSendService

public class MinBroadcastReceiver extends BroadcastReceiver {


    public MinBroadcastReceiver(){}
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context.getApplicationContext(), "I BroadcastReceiver", Toast.LENGTH_SHORT).show();
        Log.d("", "Nå er du i MinBroadCastReceiver");

        Intent i = new Intent(context, MinPeriodisk.class);
        context.startService(i);
    }



}
