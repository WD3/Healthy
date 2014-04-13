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
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		myAlarm.setAlarm();
		SaveSteps.saveSteps();
//		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
//		String date = format.format(new Date());
//		MainActivity.sp.edit().putString("����", date)
//		.putString(date, StepCounter.tvsteps)
//		.putString("����", StepCounter.tvsteps)
//		.putString("����", StepCounter.distance)
//		.putString("��·��", StepCounter.calorie)
//		.putString("����", StepCounter.progress).commit();
	}

}
