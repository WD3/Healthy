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
		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		StepCounter.steps = 0;
		StepCounter.tvsteps = "0";
		StepCounter.distance = "0";
		StepCounter.calorie = "0";
		StepCounter.progress = "0%";
		MainActivity.SendMessage(MainActivity.handler, 1);
		for(int i = 0;i<24;i++){
			MainActivity.sp.edit().putInt(i+"hoursteps", 0)
			.putInt(i+1+"fhoursteps", 0).commit();
		}
		String curDate = format.format(new Date());
		MainActivity.sp.edit().putString("日期", curDate).commit();
	}
}
