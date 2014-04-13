package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.SharedPreferences;

public class SaveSteps {
	private final int INTERVAL = 60000;
	private Timer timer;

	// ÿ���Сʱ��һ�μƲ�����
	public void save() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				saveSteps();
			}
		};
		timer = new Timer();
		timer.schedule(task, 500, INTERVAL);
	}

	public static void saveSteps() {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String date = format.format(new Date());

		MainActivity.sp.edit().putString("����", date)
				.putString(date, StepCounter.tvsteps)
				.putString("����", StepCounter.tvsteps)
				.putString("����", StepCounter.distance)
				.putString("��·��", StepCounter.calorie)
				.putString("����", StepCounter.progress).commit();
	}

	public void cancleTimer() {
		if (timer != null)
			timer.cancel();
	}
}
