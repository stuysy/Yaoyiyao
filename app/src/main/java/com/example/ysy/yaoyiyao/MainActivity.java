package com.example.ysy.yaoyiyao;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private static String strs[] = {"石头","剪刀","布"};
    private static int pics[] = {R.mipmap.shitou,R.mipmap.jiandao,R.mipmap.bu};
    private static String TAG = "MainActivity";

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    protected void onResume(){
        super.onResume();
        if (sensorManager != null){
            sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    protected void onStop(){
        super.onStop();
        if (sensorManager != null){
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {


        private static final int SENSOR_SHAKE = 10 ;

        @Override
        public void onSensorChanged(SensorEvent event) {
            //传感信息改变时执行
            float[] values = event.values;
            float x = values[0];//xyz三个轴方向上的重力加速度，向右、前、上为正
            float y = values[1];
            float z = values[2];
            Log.i(TAG, "x[ "+x+"] y["+y+"] z["+z+"]");

            int medumValue = 10;
            if (Math.abs(x)> medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue){
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 10:
                    Log.i(TAG, "检测到摇晃，执行操作！");
                    java.util.Random r = new java.util.Random();
                    int num = Math.abs(r.nextInt())%3;
                    textView.setText(strs[num]);
                    imageView.setImageResource(pics[num]);
                    break;
            }
        }
    };
}
