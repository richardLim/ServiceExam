package com.example.serviceexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MyService mService;
    private boolean mBound;

    private static final String TAG = MyService.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStartService(View view) {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    public void onStartStop(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    public void onStartIntentService(View view) {
        Intent intent = new Intent(this, MyIntentService.class);
        startService(intent);
    }

    public void onStartForgroundService(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.setAction("startForeground");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }else {
            startService(intent);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder binder = (MyService.MyBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void getCountValue(View view) {
        if(mBound) {
            Toast.makeText(this, "카운팅 : " + mService.getCount(), Toast.LENGTH_SHORT).show();
        }
    }
}