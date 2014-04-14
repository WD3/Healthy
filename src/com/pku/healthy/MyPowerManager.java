package com.pku.healthy;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class MyPowerManager {
	private PowerManager powerManager;
	private WakeLock wakeLock;
	private Context context;
	
	public MyPowerManager(Context context){
		this.context = context;
		powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SimpleTimer");		
	}
	public void acquireWakeLock(){
		if(wakeLock != null){
			wakeLock.acquire();
		}
	}
	public void releaseWakeLock(){
		if(wakeLock != null&&wakeLock.isHeld()){
			wakeLock.release();
			wakeLock = null;
		}
	}
}
