package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.SharedPreferences;

public class SaveSteps {
	private final int INTERVAL = 60000;
	private Timer timer;

	// 每半个小时存一次计步数据
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

		MainActivity.sp.edit().putString("日期", date)
				.putString(date, StepCounter.tvsteps)
				.putString("步数", StepCounter.tvsteps)
				.putString("距离", StepCounter.distance)
				.putString("卡路里", StepCounter.calorie)
				.putString("进度", StepCounter.progress).commit();
	}

	public void cancleTimer() {
		if (timer != null)
			timer.cancel();
	}
}
