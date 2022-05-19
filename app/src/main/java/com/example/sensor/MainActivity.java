package com.example.sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.security.Provider;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    AirplaneModeChangeReceiver airplane = new AirplaneModeChangeReceiver();

    private SensorManager SensorManager;
    TextView textview2;
    SensorManager sensorManager;
    Sensor accelerometersensor, lightsensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        accelerometersensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        lightsensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        //listSensors();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplane, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplane);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, accelerometersensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, lightsensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    boolean moving;
    boolean outside;

    @Override
    public void onSensorChanged(SensorEvent Event){

        int first = 2;
        String print = null;
        int send;

        if(Event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            int valuex = (int) Event.values[0];
            int valuey = (int) Event.values[1];
            int valuez = (int) Event.values[2];



            /*
            String valuex = String.format("%.2f", Event.values[0]);
            String valuey = String.format("%.2f", Event.values[1]);
            String valuez = String.format("%.2f", Event.values[2]);

             */
            TextView acceletorx = findViewById(R.id.acceletorx);
            TextView acceletory = findViewById(R.id.acceletory);
            TextView acceletorz = findViewById(R.id.acceletorz);

            acceletorx.setText("" + valuex);
            acceletory.setText("" + valuey);
            acceletorz.setText("" + valuez);

            Log.d("accelerometer", valuex + "|" + valuey + "|" + valuez);

            if(valuex != 0 || valuey != 0 || valuez != 0){

                //Moving
                moving = true;

            }
            else{

                //Not Moving
                moving = false;
            }

        }
        if(Event.sensor.getType() == Sensor.TYPE_LIGHT){
            int level = (int) Event.values[0];

            TextView light = findViewById(R.id.light);

            light.setText("" + level);
            Log.d("light", level + "");

            if(level != 0){

                //outside
                outside = true;
            }
            else{

                //at pocket
                outside = false;
            }
        }

        Intent intent = new Intent();
        intent.setAction("com.example.sensor.GET_TEL_POSITION");

        if (outside) {
            if (moving){
                print = "Telefon dışarıda ve hareketli";
            }
            else if(first != 0){
                print = "Telefon dışarıda ve hareketsiz";

                first = 0;
                send = 0;
                intent.putExtra("send", send);
            }
        } else {
            if (moving && first != 1){
                print = "Telefon cepte ve hareketli";

                first = 1;
                send = 1;
                intent.putExtra("send", send);
            }
            else{
                print = "Telefon cepte ve hareketsiz";
            }
        }
        sendBroadcast(intent);
        Log.d("tel", print);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int x){

    }
}