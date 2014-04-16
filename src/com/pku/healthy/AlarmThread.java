package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

public class AlarmThread implements Runnable{
	private Context context;
	private MyAlarm myAlarm;
	private final int SLEEP = 360000;  //23点55分数据清零后，休眠六分钟更改日期
	
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
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		MainActivity.hourStepSp.edit().clear().commit();		
		String curDate = format.format(new Date());
		MainActivity.sp.edit().putString("日期", curDate).commit();
	}
}
