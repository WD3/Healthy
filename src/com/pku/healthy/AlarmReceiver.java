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
			Date date = new Date();
			int hour = date.getHours();
			int orgSteps = MainActivity.hourStepSp.getInt(hour+"fhoursteps", 0);
			int curSteps = Integer.parseInt(StepCounter.tvsteps) - orgSteps;
			MainActivity.hourStepSp.edit().putInt(hour+"hoursteps", curSteps)
			.putInt(hour+1+"fhoursteps", Integer.parseInt(StepCounter.tvsteps)).commit();
			if(hour == 23){				
				alarmThread = new AlarmThread(context);
				new Thread(alarmThread).start();	
			}			
		}		
	}
}
