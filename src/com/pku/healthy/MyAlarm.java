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
	private final int INTERVAL = 3600000;   //ÿ��һ��Сʱ����Сʱ�������ϴ�

	public MyAlarm(Context context){
		this.context = context;
		am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
	}
	public void setAlarm() {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		date.setHours(date.getHours());
		date.setMinutes(59);
		date.setSeconds(57);
		System.out.println("date"+date);
		calendar.setTime(date);			  

		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction("AlarmReceiver");
		pi = PendingIntent.getBroadcast(context, 0, intent, 0);// ����һ��PendingIntent���󣬷��͹㲥
		// ��ȡAlarmManager����
		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),INTERVAL ,pi);
	}
	public void cancleAlarm(){
		if(pi!=null)
			am.cancel(pi);
	}
}
