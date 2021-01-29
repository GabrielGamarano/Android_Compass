package com.example.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView bussola;
    private TextView posicao, direcao;
    int posBussola;
    private SensorManager sManager;
    private Sensor mAcc, mGyro, mMag, mOrientacao;
    float[] rMat = new float[9];
    float[] orientacao = new float[9];
    private float[] lastGyro = new float[3];
    private float[] lastAcc = new float[3];
    private float[] lastMag = new float[3];
    private boolean lastAccSet = false, lastMagSet = false, lastGyroSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mAcc = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMag = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mGyro = sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mOrientacao = sManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (mAcc != null){
            sManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mMag != null){
            sManager.registerListener(this, mMag, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mOrientacao != null){
            sManager.registerListener(this, mOrientacao, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(mGyro != null){
            sManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
        }

        start();
    }

    protected void init(){
        bussola = findViewById(R.id.Bussola);
        posicao = findViewById(R.id.Posicao);
        direcao = findViewById(R.id.Direcao);
        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mSensor = event.sensor;
        if (mSensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            posBussola = (int) ((Math.toDegrees(SensorManager.getOrientation(rMat, orientacao)[0])+360)%360);

        }
        if(mSensor.getType() == Sensor.TYPE_ACCELEROMETER){
            System.arraycopy(event.values,0,lastAcc,0, event.values.length);
            lastAccSet = true;

        }else if (mSensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            System.arraycopy(event.values,0,lastMag,0,event.values.length);
            lastMagSet = true;
        }if (mSensor.getType() == Sensor.TYPE_GYROSCOPE){
            System.arraycopy(event.values,0,lastGyro,0,event.values.length);
            lastGyroSet = true;
        }
        if (lastAccSet && lastMagSet == true){
            SensorManager.getRotationMatrix(rMat,null,lastAcc,lastMag);
            SensorManager.getOrientation(rMat, orientacao);
            posBussola = (int) ((Math.toDegrees(SensorManager.getOrientation(rMat, orientacao)[0])+360)%360);
        }


        posBussola = Math.round(posBussola);
        bussola.setRotation(-posBussola);

        String onde = "NO";

        if(posBussola >= 350 || posBussola <= 10){
            onde = "Norte";
        }
        if (posBussola < 350 && posBussola >280){
            onde = "Noroeste";
        }
        if(posBussola <=280 && posBussola > 260){
            onde= "Oeste";
        }
        if(posBussola <=260 && posBussola > 190){
            onde = "Sudoeste";
        }
        if(posBussola <=190 && posBussola > 170){
            onde = "Sul";
        }
        if (posBussola <=170 && posBussola > 100){
            onde ="Sudeste";
        }
        if (posBussola <=100 && posBussola > 80){
            onde = "Leste";
        }
        if (posBussola <=80 && posBussola > 10){
            onde = "Nordeste";
        }

        String direct = "NO";

        if(posBussola >= 350 || posBussola <= 10){
            direct = "Frente";
        }
        if (posBussola < 350 && posBussola >280){
            direct = "Diagonal Sup Esquerda";
        }
        if(posBussola <=280 && posBussola > 260){
            direct= "Esquerda";
        }
        if(posBussola <=260 && posBussola > 190){
            direct= "Diagonal Inf Esquerda";
        }
        if(posBussola <=190 && posBussola > 170){
            direct= "Baixo";
        }
        if (posBussola <=170 && posBussola > 100){
            direct="Diagonal Inf Direita";
        }
        if (posBussola <=100 && posBussola > 80){
            direct= "Direita";
        }
        if (posBussola <=80 && posBussola > 10){
            direct= "Diagonal Sup Direita";
        }

        posicao.setText(posBussola + "º" + onde);
        direcao.setText(direct);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void start(){
        if ( mOrientacao == null) {
            if ( mMag== null || mAcc == null){
                posicao.setText("Sensor não encontrado");
            }
        }
    }
}