package com.pku.healthy;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class LockScreen {
	private Context context;
	private WakeLock wakeLock = null;

	public LockScreen(Context context){
		this.context = context;
	}
	public void acquireWakeLock(){
		if(wakeLock == null){
			PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "tag");
		}
		if(wakeLock != null)
			wakeLock.acquire();
	}
	public void releaseWakeLock(){
		if(wakeLock != null&&wakeLock.isHeld()){
			wakeLock.release();
			wakeLock = null;
		}
	}
}
