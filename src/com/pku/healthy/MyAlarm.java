package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class MyAlarm {
	private Context context;
	private AlarmManager am;
	private PendingIntent pi;

	public MyAlarm(Context context){
		this.context = context;
		am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
	}
	public void setAlarm() {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(58);
		calendar.setTime(date);			  

		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction("AlarmReceiver");
		pi = PendingIntent.getBroadcast(context, 0, intent, 0);// 设置一个PendingIntent对象，发送广播
		// 获取AlarmManager对象
		am.set(AlarmManager.RTC, calendar.getTimeInMillis(), pi);
	}
	public void cancleAlarm(){
		if(pi!=null)
			am.cancel(pi);
	}
}
