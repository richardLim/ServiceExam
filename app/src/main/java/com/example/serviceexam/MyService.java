package com.example.serviceexam;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.constraintlayout.motion.utils.MonotonicCurveFit;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    private Thread mThread;
    private int mCount = 0;

    private IBinder mBinder = new MyBinder();
    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if("startForeground".equals(intent.getAction())) {
            startForegroundService();
            Log.d("My Thread", "포그라운드 서비스 시작 ");
        } else if(mThread == null) {
            mThread = new Thread("My Thread") {
                @Override
                public void run() {
                    for(int i = 0; i < 100; i++) {
                        mCount++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                        Log.d("My Thread", "서비스 동작중 " + mCount);
                    }
                }
            };
            mThread.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

        if(mThread != null) {
            mThread.interrupt();
            mThread = null;
            mCount = 0;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public int getCount() {
        return mCount;
    }


    private void startForegroundService() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("포그라운드 서비스");
        builder.setContentText("포그라운드 서비스 실행중");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        startForeground(1, builder.build());
    }



}