package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	private AlarmThread alarmThread;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("AlarmReceiver")){
			SimpleDateFormat format = new SimpleDateFormat("MM-dd");
			String date = format.format(new Date());
			MainActivity.sp.edit().putString(date, StepCounter.tvsteps).commit();
			StepCounter.steps = 0;
			StepCounter.tvsteps = "0";
			StepCounter.distance = "0";
			StepCounter.calorie = "0";
			StepCounter.progress = "0%";
			MainActivity.SendMessage(MainActivity.handler, 1);
			System.out.println("º∆≤Ω«Â¡„");
			alarmThread = new AlarmThread(context);
			new Thread(alarmThread).start();			
		}		
	}

}
