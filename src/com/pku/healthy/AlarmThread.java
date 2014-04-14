package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

public class AlarmThread implements Runnable{
	private Context context;
	private MyAlarm myAlarm;
	
	public AlarmThread(Context context){
		this.context = context;
		myAlarm = new MyAlarm(context);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String orgDate = format.format(new Date());
		MainActivity.sp.edit().putString(orgDate, StepCounter.tvsteps).commit();
		StepCounter.steps = 0;
		StepCounter.tvsteps = "0";
		StepCounter.distance = "0";
		StepCounter.calorie = "0";
		StepCounter.progress = "0%";
		MainActivity.SendMessage(MainActivity.handler, 1);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		MainActivity.hourStepSp.edit().clear().commit();		
		String curDate = format.format(new Date());
		MainActivity.sp.edit().putString("ÈÕÆÚ", curDate).commit();
	}
}
