package com.pku.healthy;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.SharedPreferences;

public class ExitManager {

	public static void save() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String sDate = format.format(date);
		int hour = date.getHours();
//		int orgSteps = MainActivity.sp.getInt(hour+"fhoursteps", 0);
//		int curSteps = Integer.parseInt(StepCounter.tvsteps) - orgSteps;
//		MainActivity.sp.edit().putInt(hour+"hoursteps", curSteps).commit();
		PlayService.sp.edit().putString("日期", sDate)
				.putString(sDate, StepCounter.tvsteps)
				.putString("步数", StepCounter.tvsteps)
				.putString("距离", StepCounter.distance)
				.putString("卡路里", StepCounter.calorie)
				.putString("进度", StepCounter.progress).commit();
	}
}
