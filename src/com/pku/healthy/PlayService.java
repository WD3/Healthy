package com.pku.healthy;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

public class PlayService extends Service{
	private StepCounter stepCounter;
	private SensorManager mSensorManager;
	private Sensor sLinearAcceleration;
	private MyPowerManager myPowerManager;
	private MyAlarm myAlarm;
	private NotificationExtend notification;
	static SharedPreferences sp;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public void onCreate(){
		super.onCreate();
		Log.e("PlayService", "PlayService onCreate");
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		stepCounter = new StepCounter();
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    sLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	    myPowerManager = new MyPowerManager(this);
	    mSensorManager.registerListener(stepCounter, sLinearAcceleration, SensorManager.SENSOR_DELAY_FASTEST);
	    myPowerManager.acquireWakeLock();
	    stepCounter.start();
		CountSet.countSet();
		myAlarm = new MyAlarm(this);
		myAlarm.setAlarm();
		notification = new NotificationExtend(this);
	}

	public void onDestroy(){
		super.onDestroy();
		Log.e("PlayService", "PlayService onDestroy");
    	mSensorManager.unregisterListener(stepCounter);
    	myPowerManager.releaseWakeLock();
    	myAlarm.cancleAlarm();
    	ExitManager.save();
    	stepCounter.stop();
    	notification.cancel();
    	System.exit(0);
	}
}
