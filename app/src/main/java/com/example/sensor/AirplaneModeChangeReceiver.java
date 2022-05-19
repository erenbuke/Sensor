package com.example.sensor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

public class AirplaneModeChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(isOn(context.getApplicationContext())){
            Toast.makeText(context, "Airplane Mode is ON", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context, "Airplane Mode is OFF", Toast.LENGTH_LONG).show();
        }
    }

    private static boolean isOn(Context context){
        return Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }
}
